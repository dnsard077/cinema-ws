package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.*;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.User;
import com.example.cinema.cinemaws.repository.UserRepository;
import com.example.cinema.cinemaws.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final ValidationService validationService;
    private final RedisService redisService;
    private final OtpService otpService;

    public String registerSend(UserRegistrationTO request) {
        validationService.validate(request);

        String registerId = UUID.randomUUID().toString();
        String key = String.join(":", "register", registerId, "key");
        try {
            redisService.set(key, objectMapper.writeValueAsString(request), Duration.ofHours(1));
        } catch (JsonProcessingException e) {
            throw new ResponseException(ResponseCodeEn.OPERATION_FAILED);
        }

        otpService.sendOtp(OtpSendTO.builder()
                .email(request.email())
                .trxType(TransactionTypeEn.REGISTER)
                .name(request.username()).build());
        return registerId;
    }

    public void registerVerify(UserVerificationTO request) {
        String key = String.join(":", "register", request.registerId(), "key");
        Optional<String> registerData = Optional.ofNullable(redisService.get(key));
        if (registerData.isEmpty()) {
            throw new ResponseException(ResponseCodeEn.INVALID_ID);
        }

        UserRegistrationTO userData = null;
        try {
            userData = objectMapper.readValue(registerData.get(), UserRegistrationTO.class);
        } catch (Exception e) {
            throw new ResponseException(ResponseCodeEn.OPERATION_FAILED);
        }

        otpService.verifyOtp(OtpVerifyTO.builder()
                .email(userData.email())
                .name(userData.username())
                .trxType(TransactionTypeEn.REGISTER)
                .otp(request.otp())
                .build());

        User user = new User();
        user.setEmail(userData.email());
        String encodedPassword = passwordEncoder.encode(userData.password());
        user.setPassword(encodedPassword);
        user.setUsername(userData.username());
        user.setRole(User.Role.CUSTOMER);
        userRepository.save(user);
    }

    public TokenResTO login(LoginReqTO request) {
        validationService.validate(request);
        User user = userRepository.findByUsername(request.username()).orElseThrow(() -> new ResponseException(ResponseCodeEn.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseException(ResponseCodeEn.INVALID_CREDENTIALS);
        }
        Map<String, Object> claims = convertUserToClaims(user);
        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), claims);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), claims);

        return new TokenResTO(accessToken, refreshToken);
    }

    public TokenResTO refreshToken(RefreshTokenTO refreshTokenTO) {
        validationService.validate(refreshTokenTO);

        try {
            String username = jwtUtil.extractUsername(refreshTokenTO.refreshToken());

            if (jwtUtil.validateToken(refreshTokenTO.refreshToken(), username)) {
                User user = userRepository.findByUsername(username).orElseThrow(() ->
                        new ResponseException(ResponseCodeEn.USER_NOT_FOUND)
                );
                Map<String, Object> claims = convertUserToClaims(user);
                String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(), claims);
                String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername(), claims);

                return new TokenResTO(newAccessToken, newRefreshToken);
            } else {
                throw new ResponseException(ResponseCodeEn.OPERATION_FAILED);
            }
        } catch (Exception e) {
            throw new ResponseException(ResponseCodeEn.OPERATION_FAILED);
        }
    }

    public Map<String, Object> convertUserToClaims(User user) {
        AccessTokenTO accessTokenTO = objectMapper.convertValue(user, AccessTokenTO.class);
        return objectMapper.convertValue(accessTokenTO, new TypeReference<Map<String, Object>>() {});
    }
}

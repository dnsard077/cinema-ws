package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.*;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.User;
import com.example.cinema.cinemaws.repository.UserRepository;
import com.example.cinema.cinemaws.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ValidationService validationService;

    @Mock
    private RedisService redisService;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidRegistrationRequest_whenRegisterSend_thenReturnRegisterVerifyId() throws Exception {
        UserRegistrationTO request = new UserRegistrationTO("username", "password", "email@example.com");
        when(objectMapper.writeValueAsString(request)).thenReturn("jsonString");

        String registerId = authService.registerSend(request);

        assertNotNull(registerId);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        verify(redisService).set(keyCaptor.capture(), valueCaptor.capture(), durationCaptor.capture());

        String expectedKey = String.join(":", "register", registerId, "key");
        assertEquals(expectedKey, keyCaptor.getValue());
        assertEquals("jsonString", valueCaptor.getValue());
        assertEquals(Duration.ofHours(1), durationCaptor.getValue());

        verify(otpService).sendOtp(any(OtpSendTO.class));
    }

    @Test
    public void givenValidVerificationRequest_whenRegisterVerify_thenUserIsSaved() throws Exception {
        String registerId = UUID.randomUUID().toString();
        UserRegistrationTO userData = new UserRegistrationTO("username", "password", "email@example.com");
        String key = String.join(":", "register", registerId, "key");
        when(redisService.get(key)).thenReturn("jsonString");
        when(objectMapper.readValue("jsonString", UserRegistrationTO.class)).thenReturn(userData);
        when(passwordEncoder.encode(userData.password())).thenReturn("encodedPassword");

        UserVerificationTO request = new UserVerificationTO(registerId, "otp");

        authService.registerVerify(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void givenInvalidOtp_whenRegisterVerify_thenThrowException() throws JsonProcessingException {
        String registerId = UUID.randomUUID().toString();
        UserRegistrationTO userData = new UserRegistrationTO("username", "password", "email@example.com");
        String key = String.join(":", "register", registerId, "key");
        when(redisService.get(key)).thenReturn("jsonString");
        when(objectMapper.readValue("jsonString", UserRegistrationTO.class)).thenReturn(userData);
        doThrow(new ResponseException(ResponseCodeEn.USER_WRONG_PASSWORD)).when(otpService).verifyOtp(any());

        UserVerificationTO request = new UserVerificationTO(registerId, "otp");

        assertThrows(ResponseException.class, () -> authService.registerVerify(request));
    }

    @Test
    void givenValidLoginRequest_whenLogin_thenReturnToken() {
        // Given
        LoginReqTO request = new LoginReqTO("username", "password");
        User user = new User();
        user.setUserId(1L);
        user.setUsername("username");
        user.setPassword("encodedPassword");
        user.setEmail("email@example.com");
        user.setRole(User.Role.CUSTOMER);

        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);

        // Create claims map
        Map<String, Object> claims = null;

        // Stub the convertUser ToClaims method correctly
        when(authService.convertUserToClaims(user)).thenReturn(claims);

        // Stub JWT token generation
        when(jwtUtil.generateAccessToken(user.getUsername(), claims)).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(user.getUsername(), claims)).thenReturn("refreshToken");

        // When
        TokenResTO result = authService.login(request);

        // Then
        assertNotNull(result);
        assertEquals("accessToken", result.token());
        assertEquals("refreshToken", result.refreshToken());
    }

    @Test
    public void givenInvalidUsername_whenLogin_thenThrowException() {
        LoginReqTO request = new LoginReqTO("invalidUsername", "password");
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());

        assertThrows(ResponseException.class, () -> authService.login(request));
    }

    @Test
    public void givenInvalidPassword_whenLogin_thenThrowException() {
        LoginReqTO request = new LoginReqTO("username", "wrongPassword");
        User user = new User();
        user.setUsername("username");
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        assertThrows(ResponseException.class, () -> authService.login(request));
    }

    @Test
    public void givenValidRefreshToken_whenRefreshToken_thenReturnNewTokens() {
        RefreshTokenTO refreshTokenTO = new RefreshTokenTO("validRefreshToken");
        String username = "username";
        User user = new User();
        user.setUsername(username);

        when(jwtUtil.extractUsername(refreshTokenTO.refreshToken())).thenReturn(username);
        when(jwtUtil.validateToken(refreshTokenTO.refreshToken(), username)).thenReturn(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Map<String, Object> claims = null;
        when(jwtUtil.generateAccessToken(user.getUsername(), claims)).thenReturn("newAccessToken");
        when(jwtUtil.generateRefreshToken(user.getUsername(), claims)).thenReturn("newRefreshToken");

        TokenResTO result = authService.refreshToken(refreshTokenTO);

        assertNotNull(result);
        assertEquals("newAccessToken", result.token());
        assertEquals("newRefreshToken", result.refreshToken());
    }

    @Test
    public void givenInvalidRefreshToken_whenRefreshToken_thenThrowException() {
        RefreshTokenTO refreshTokenTO = new RefreshTokenTO("invalidRefreshToken");
        when(jwtUtil.extractUsername(refreshTokenTO.refreshToken())).thenReturn("username");
        when(jwtUtil.validateToken(refreshTokenTO.refreshToken(), "username")).thenReturn(false);

        assertThrows(ResponseException.class, () -> authService.refreshToken(refreshTokenTO));
    }
}

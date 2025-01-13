package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.EmailReqTO;
import com.example.cinema.cinemaws.dto.OtpSendTO;
import com.example.cinema.cinemaws.dto.OtpVerifyTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.util.EmailTemplate;
import com.example.cinema.cinemaws.util.Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {
    private final EmailService emailService;
    private final RedisService redisService;

    private final String OTP_PREFIX = "otp";

    public void sendOtp(OtpSendTO request) {
        String otp = Helper.generateRandomOTP(6);
        String key = String.join(":", OTP_PREFIX, String.valueOf(request.trxType()), request.name(), "key");
        String countKey = String.join(":", OTP_PREFIX, String.valueOf(request.trxType()), request.name(), "count");
        String cooldownKey = String.join(":", OTP_PREFIX, String.valueOf(request.trxType()), request.name(), "cooldown");

        if (redisService.isExists(key)) {
            throw new ResponseException(ResponseCodeEn.OTP_SENT);
        }

        if (redisService.isExists(cooldownKey)) {
            throw new ResponseException(ResponseCodeEn.OTP_COOLDOWN);
        }

        int requestCount = Integer.parseInt(Optional.ofNullable(redisService.get(countKey)).orElse("0"));

        if (requestCount >= 3) {
            redisService.set(cooldownKey, "1", Duration.ofMinutes(5));
            throw new ResponseException(ResponseCodeEn.OTP_LIMIT);
        }

        redisService.set(countKey, String.valueOf(requestCount + 1), Duration.ofMinutes(3));

        Map<String, String> values = new HashMap<>();
        values.put("name", request.name());
        values.put("trxType", String.valueOf(request.trxType()));
        values.put("otp", otp);
        String content = EmailTemplate.getOtpEmailTemplate(request.trxType(), values);

        EmailReqTO emailReqTO = EmailReqTO.builder()
                .to(request.email())
                .subject("Example Cinema Transaction OTP")
                .content(content)
                .build();

        emailService.sendEmail(emailReqTO);
        redisService.set(key, otp, Duration.ofMinutes(2));
    }

    public void verifyOtp(OtpVerifyTO request) {
        String key = String.join(":", OTP_PREFIX, String.valueOf(request.trxType()), request.name(), "key");

        if (!redisService.isExists(key)) {
            throw new ResponseException(ResponseCodeEn.OTP_FAILED);
        }

        String existingOtp = Optional.ofNullable(redisService.get(key))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "OTP does not match!"));

        if (!existingOtp.equals(request.otp())) {
            throw new ResponseException(ResponseCodeEn.OTP_FAILED);
        }
        log.info("OTP verified!");
    }
}

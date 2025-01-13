package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.EmailReqTO;
import com.example.cinema.cinemaws.dto.OtpSendTO;
import com.example.cinema.cinemaws.dto.OtpVerifyTO;
import com.example.cinema.cinemaws.dto.TransactionTypeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OtpServiceTest {

    @InjectMocks
    private OtpService otpService;

    @Mock
    private EmailService emailService;

    @Mock
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidOtpSendRequest_whenSendOtp_thenEmailIsSent() {
        // Given
        OtpSendTO request = new OtpSendTO("email@example.com", "username", TransactionTypeEn.REGISTER);
        when(redisService.isExists(any())).thenReturn(false);
        when(redisService.isExists(any())).thenReturn(false);
        when(redisService.get(any())).thenReturn("0");

        // When
        otpService.sendOtp(request);

        // Then
        verify(redisService, times(1)).set(contains("otp:REGISTER:username:key"), any(), any(Duration.class));
        verify(redisService, times(1)).set(contains("otp:REGISTER:username:count"), any(), any(Duration.class));
        verify(emailService).sendEmail(any(EmailReqTO.class));
    }

    @Test
    void givenExistingOtp_whenSendOtp_thenThrowOtpSentException() {
        // Given
        OtpSendTO request = new OtpSendTO("email@example.com", "transactionType", TransactionTypeEn.REGISTER);
        when(redisService.isExists(any())).thenReturn(true);

        // When & Then
        assertThrows(ResponseException.class, () -> otpService.sendOtp(request));
    }

    @Test
    void givenCooldownActive_whenSendOtp_thenThrowOtpCooldownException() {
        // Given
        OtpSendTO request = new OtpSendTO("email@example.com", "transactionType", TransactionTypeEn.REGISTER);
        when(redisService.isExists(any())).thenReturn(false);
        when(redisService.isExists(any())).thenReturn(true);

        // When & Then
        assertThrows(ResponseException.class, () -> otpService.sendOtp(request));
    }

    @Test
    void givenRequestCountExceeded_whenSendOtp_thenThrowOtpLimitException() {
        // Given
        OtpSendTO request = new OtpSendTO("email@example.com", "transactionType", TransactionTypeEn.REGISTER);
        when(redisService.isExists(any())).thenReturn(false);
        when(redisService.isExists(any())).thenReturn(false);
        when(redisService.get(any())).thenReturn("3");

        // When & Then
        assertThrows(ResponseException.class, () -> otpService.sendOtp(request));
    }

    @Test
    void givenValidOtpVerifyRequest_whenVerifyOtp_thenSuccess() {
        // Given
        OtpVerifyTO request = new OtpVerifyTO("username@mail.com", "username", TransactionTypeEn.REGISTER, "123456");
        String key = String.join(":", "otp", String.valueOf(request.trxType()), request.name(), "key");

        when(redisService.isExists(key)).thenReturn(true);
        when(redisService.get(key)).thenReturn("123456");

        // When
        otpService.verifyOtp(request);

        // Then
        // No exception thrown, so we can assert success
    }

    @Test
    void givenInvalidOtp_whenVerifyOtp_thenThrowOtpFailedException() {
        // Given
        OtpVerifyTO request = new OtpVerifyTO("username", "transactionType", TransactionTypeEn.REGISTER, "wrongOtp");
        String key = "otp:transactionType:username:key";
        when(redisService.isExists(key)).thenReturn(true);
        when(redisService.get(key)).thenReturn("123456");

        // When & Then
        assertThrows(ResponseException.class, () -> otpService.verifyOtp(request));
    }

    @Test
    void givenNonExistentOtp_whenVerifyOtp_thenThrowOtpFailedException() {
        // Given
        OtpVerifyTO request = new OtpVerifyTO("username", "transactionType", TransactionTypeEn.REGISTER, "123456");
        String key = "otp:transactionType:username:key";
        when(redisService.isExists(key)).thenReturn(false);

        // When & Then
        assertThrows(ResponseException.class, () -> otpService.verifyOtp(request));
    }
}
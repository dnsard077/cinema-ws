package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.*;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.OtpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class OtpControllerTest {

    @InjectMocks
    private OtpController otpController;

    @Mock
    private OtpService otpService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenOtpSendRequest_whenSendOtp_thenReturnSuccessResponse() {
        // Given
        OtpSendTO request = new OtpSendTO("user@mail.com", "user", TransactionTypeEn.REGISTER); // Adjust this based on the actual fields in OtpSendTO
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(null)
                .build();

        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION)).thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = otpController.sendOtp(request);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenOtpVerifyRequest_whenVerifyOtp_thenReturnSuccessResponse() {
        // Given
        OtpVerifyTO request = new OtpVerifyTO("user@mail.com", "user", TransactionTypeEn.REGISTER, "123456"); // Adjust this based on the actual fields in OtpVerifyTO
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(null)
                .build();

        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION)).thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = otpController.verifyOtp(request);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }
}
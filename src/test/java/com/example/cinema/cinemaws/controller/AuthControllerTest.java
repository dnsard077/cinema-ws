package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.*;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenUserRegistrationRequest_whenRegisterSend_thenReturnSuccessResponse() {
        // Given
        UserRegistrationTO request = new UserRegistrationTO("user", "user@mail.com", "pass");
        String expectedResponse = "expected";
        when(authService.registerSend(request)).thenReturn(expectedResponse);

        ApiResponseTO<String> expectedApiResponse = ApiResponseTO.<String>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(expectedResponse)
                .build();

        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, expectedResponse))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = authController.registerSend(request);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenUserVerificationRequest_whenRegisterVerify_thenReturnSuccessResponse() {
        // Given
        UserVerificationTO request = new UserVerificationTO("user@mail.com", "otpCode");
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(null)
                .build();

        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION)).thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = authController.registerVerify(request);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenLoginRequest_whenLogin_thenReturnSuccessResponse() {
        // Given
        LoginReqTO request = new LoginReqTO("user@mail.com", "pass");
        TokenResTO expectedTokenResponse = new TokenResTO("token", "refreshToken");
        ApiResponseTO<TokenResTO> expectedApiResponse = ApiResponseTO.<TokenResTO>builder()
                .code(ResponseCodeEn.USER_LOGIN.getHttpStatus().value())
                .responseCode(ResponseCodeEn.USER_LOGIN.getCode())
                .message("success.login")
                .data(expectedTokenResponse)
                .build();

        when(authService.login(request)).thenReturn(expectedTokenResponse);
        when(apiResponseFactory.createResponse(ResponseCodeEn.USER_LOGIN, expectedTokenResponse))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<TokenResTO>> response = authController.login(request);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenRefreshTokenRequest_whenRefreshToken_thenReturnSuccessResponse() {
        // Given
        RefreshTokenTO refreshTokenTO = new RefreshTokenTO("refreshToken");
        TokenResTO expectedTokenResponse = new TokenResTO("newToken", "refreshToken");
        ApiResponseTO<TokenResTO> expectedApiResponse = ApiResponseTO.<TokenResTO>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(expectedTokenResponse)
                .build();

        when(authService.refreshToken(refreshTokenTO)).thenReturn(expectedTokenResponse);
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, expectedTokenResponse))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<TokenResTO>> response = authController.refreshToken(refreshTokenTO);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }
}
package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.*;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final ApiResponseFactory apiResponseFactory;

    @PostMapping("/register/send")
    public ResponseEntity<ApiResponseTO<Object>> registerSend(@RequestBody UserRegistrationTO request) {
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, authService.registerSend(request));
    }

    @PostMapping("/register/verify")
    public ResponseEntity<ApiResponseTO<Object>> registerVerify(@RequestBody UserVerificationTO request) {
        authService.registerVerify(request);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseTO<TokenResTO>> login(@RequestBody LoginReqTO request) {
        return apiResponseFactory.createResponse(ResponseCodeEn.USER_LOGIN, authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseTO<TokenResTO>> refreshToken(@RequestBody RefreshTokenTO refreshTokenTO) {
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, authService.refreshToken(refreshTokenTO));
    }
}

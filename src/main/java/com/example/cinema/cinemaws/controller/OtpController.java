package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.OtpSendTO;
import com.example.cinema.cinemaws.dto.OtpVerifyTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.EmailService;
import com.example.cinema.cinemaws.service.OtpService;
import com.example.cinema.cinemaws.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/otp")
public class OtpController {
    private final OtpService otpService;
    private final ApiResponseFactory apiResponseFactory;

    @PostMapping("/send")
    public ResponseEntity<ApiResponseTO<Object>> sendOtp(@RequestBody OtpSendTO request) {
        otpService.sendOtp(request);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponseTO<Object>> verifyOtp(@RequestBody OtpVerifyTO request) {
        otpService.verifyOtp(request);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION);

    }
}

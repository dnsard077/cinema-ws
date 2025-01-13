package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiResponseFactory {
    private final MessageSource messageSource;

    public <T> ResponseEntity<ApiResponseTO<T>> createResponse(ResponseCodeEn responseCode) {
        return createResponse(responseCode, LocaleContextHolder.getLocale(), null, null);
    }

    public <T> ResponseEntity<ApiResponseTO<T>> createResponse(ResponseCodeEn responseCode, T data) {
        return createResponse(responseCode, LocaleContextHolder.getLocale(), data, null);
    }

    public <T> ResponseEntity<ApiResponseTO<T>> createResponse(ResponseCodeEn responseCode, Locale locale, T data) {
        return createResponse(responseCode, locale, data, null);
    }

    public <T> ResponseEntity<ApiResponseTO<T>> createResponse(ResponseCodeEn responseCode, T data, Map<String, List<String>> error) {
        return createResponse(responseCode, LocaleContextHolder.getLocale(), data, error);
    }

    public <T> ResponseEntity<ApiResponseTO<T>> createResponse(ResponseCodeEn responseCode, Locale locale, T data, Map<String, List<String>> error) {
        String message = messageSource.getMessage(responseCode.getMessageKey(), null, locale);
        if (message == null || message.isEmpty()) {
            message = "An unexpected error occurred.";
        }
        ApiResponseTO<T> responseBody = ApiResponseTO.<T>builder()
                .code(responseCode.getHttpStatus().value())
                .responseCode(responseCode.getCode())
                .message(message)
                .data(data)
                .error(error)
                .build();
        return new ResponseEntity<>(responseBody, responseCode.getHttpStatus());
    }

    public <T> ResponseEntity<ApiResponseTO<T>> createResponse(ResponseCodeEn responseCode, Map<String, List<String>> error) {
        return createResponse(responseCode, LocaleContextHolder.getLocale(), null, error);
    }
}
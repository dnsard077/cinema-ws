package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorController {
    private final ApiResponseFactory apiResponseFactory;

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ApiResponseTO<String>> handle(ResponseException ex) {
        return apiResponseFactory.createResponse(ex.getResponseCodeEn());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponseTO<String>> handle(ResponseStatusException ex) {
        return apiResponseFactory.createResponse(ResponseCodeEn.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseTO<Map<String, List<String>>>> constraintViolationException(ConstraintViolationException exception) {
        Map<String, List<String>> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.computeIfAbsent(propertyPath, k -> new ArrayList<>()).add(message);
        }
        return apiResponseFactory.createResponse(ResponseCodeEn.CONSTRAINT_VIOLATION, LocaleContextHolder.getLocale(), null, errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseTO<Map<String, String>>> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String detailedMessage = exception.getMostSpecificCause().getMessage();
        String fieldName = "unknown_field";

        if (detailedMessage.contains("column")) {
            try {
                int startIndex = detailedMessage.indexOf("column") + 7;
                int endIndex = detailedMessage.indexOf(" ", startIndex);
                fieldName = detailedMessage.substring(startIndex, endIndex);
            } catch (Exception e) {
                fieldName = "unknown_field";
            }
        }
        Map<String, String> errors = new HashMap<>();
        errors.put(fieldName, detailedMessage);
        return apiResponseFactory.createResponse(ResponseCodeEn.CONSTRAINT_VIOLATION, null, errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseTO<String>> handleMissingParams(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        String errorMessage = String.format("Missing required parameter: %s", parameterName);
        Map<String, List<String>> errors = new HashMap<>();
        errors.put(parameterName, Collections.singletonList(errorMessage));
        return apiResponseFactory.createResponse(ResponseCodeEn.INVALID_INPUT, LocaleContextHolder.getLocale(), null, errors);
    }
}

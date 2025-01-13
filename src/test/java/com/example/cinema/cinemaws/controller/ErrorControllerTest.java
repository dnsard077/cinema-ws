package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ErrorControllerTest {

    @InjectMocks
    private ErrorController errorController;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenResponseException_whenHandle_thenReturnApiResponse() {
        // Given
        ResponseException exception = new ResponseException(ResponseCodeEn.INVALID_INPUT);
        ResponseEntity<ApiResponseTO<Object>> expectedResponse = ResponseEntity.status(ResponseCodeEn.INVALID_INPUT.getHttpStatus()).build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.INVALID_INPUT))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponseTO<String>> response = errorController.handle(exception);

        // Then
        assertEquals(expectedResponse, response);
    }

    @Test
    void givenResponseStatusException_whenHandle_thenReturnApiResponse() {
        // Given
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred");
        ApiResponseTO<String> expectedResponseBody = ApiResponseTO.<String>builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .responseCode(ResponseCodeEn.INTERNAL_SERVER_ERROR.getCode())
                .message("Error occurred")
                .data(null)
                .build();
        ResponseEntity<ApiResponseTO<String>> expectedResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(expectedResponseBody);

        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.INTERNAL_SERVER_ERROR), any(String.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponseTO<String>> response = errorController.handle(exception);

        // Then
        assertEquals(expectedResponse, response);
    }

    @Test
    void givenConstraintViolationException_whenHandle_thenReturnApiResponse() {
        // Given
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);

        // Mock the property path to return a valid object
        jakarta.validation.Path propertyPath = mock(jakarta.validation.Path.class);
        when(violation.getPropertyPath()).thenReturn(propertyPath);
        when(propertyPath.toString()).thenReturn("fieldName");

        when(violation.getMessage()).thenReturn("Invalid value");
        violations.add(violation);

        ConstraintViolationException exception = new ConstraintViolationException(violations);
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("fieldName", Collections.singletonList("Invalid value"));

        ResponseEntity<ApiResponseTO<Object>> expectedResponse = ResponseEntity.status(ResponseCodeEn.CONSTRAINT_VIOLATION.getHttpStatus()).build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.CONSTRAINT_VIOLATION), any(Locale.class), any(), eq(errors)))
                .thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponseTO<Map<String, List<String>>>> response = errorController.constraintViolationException(exception);

        // Then
        assertEquals(expectedResponse, response);
    }

    @Test
    void givenMissingServletRequestParameterException_whenHandle_thenReturnApiResponse() {
        // Given
        String parameterName = "requiredParam";
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException(parameterName, "String");

        Map<String, List<String>> errors = new HashMap<>();
        errors.put(parameterName, Collections.singletonList("Missing required parameter: " + parameterName));

        ResponseEntity<ApiResponseTO<Object>> expectedResponse = ResponseEntity.status(ResponseCodeEn.INVALID_INPUT.getHttpStatus()).build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.INVALID_INPUT, Locale.getDefault(), null, errors)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponseTO<String>> response = errorController.handleMissingParams(exception);

        // Then
        assertEquals(expectedResponse, response);
    }
}
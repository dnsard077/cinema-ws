package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ApiResponseFactoryTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ApiResponseFactory apiResponseFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenResponseCode_whenCreateResponseWithCodeOnly_thenReturnApiResponse() {
        // Given
        ResponseCodeEn responseCode = ResponseCodeEn.SUCCESS_OPERATION;
        when(messageSource.getMessage(responseCode.getMessageKey(), null, LocaleContextHolder.getLocale())).thenReturn("Operation completed successfully.");

        // When
        ResponseEntity<ApiResponseTO<Object>> response = apiResponseFactory.createResponse(responseCode);

        // Then
        ApiResponseTO<Object> responseBody = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseCode.getHttpStatus().value(), responseBody.code());
        assertEquals(responseCode.getCode(), responseBody.responseCode());
        assertEquals("Operation completed successfully.", responseBody.message());
        assertEquals(null, responseBody.data());
        assertEquals(null, responseBody.error());
    }

    @Test
    public void givenResponseCodeAndData_whenCreateResponseWithCodeAndData_thenReturnApiResponse() {
        // Given
        ResponseCodeEn responseCode = ResponseCodeEn.SUCCESS_OPERATION;
        String data = "Sample Data";
        when(messageSource.getMessage(responseCode.getMessageKey(), null, LocaleContextHolder.getLocale())).thenReturn("Success message");

        // When
        ResponseEntity<ApiResponseTO<String>> response = apiResponseFactory.createResponse(responseCode, data);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseCode.getHttpStatus().value(), response.getBody().code());
        assertEquals(responseCode.getCode(), response.getBody().responseCode());
        assertEquals("Success message", response.getBody().message());
        assertEquals(data, response.getBody().data());
        assertEquals(null, response.getBody().error());
    }

    @Test
    public void givenResponseCodeAndError_whenCreateResponseWithCodeAndError_thenReturnApiResponse() {
        // Given
        ResponseCodeEn responseCode = ResponseCodeEn.CONSTRAINT_VIOLATION;
        Map<String, List<String>> error = Map.of("field", List.of("Error message"));
        when(messageSource.getMessage(responseCode.getMessageKey(), null, LocaleContextHolder.getLocale())).thenReturn("Bad request message");

        // When
        ResponseEntity<ApiResponseTO<Object>> response = apiResponseFactory.createResponse(responseCode, error);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(responseCode.getHttpStatus().value(), response.getBody().code());
        assertEquals(responseCode.getCode(), response.getBody().responseCode());
        assertEquals("Bad request message", response.getBody().message());
        assertEquals(null, response.getBody().data());
        assertEquals(error, response.getBody().error());
    }

    @Test
    public void givenResponseCodeAndLocale_whenCreateResponseWithCodeAndLocale_thenReturnApiResponse() {
        // Given
        ResponseCodeEn responseCode = ResponseCodeEn.SUCCESS_OPERATION;
        when(messageSource.getMessage(responseCode.getMessageKey(), null, LocaleContextHolder.getLocale())).thenReturn("Success message");

        // When
        ResponseEntity<ApiResponseTO<Object>> response = apiResponseFactory.createResponse(responseCode, LocaleContextHolder.getLocale(), null, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseCode.getHttpStatus().value(), response.getBody().code());
        assertEquals(responseCode.getCode(), response.getBody().responseCode());
        assertEquals("Success message", response.getBody().message());
        assertEquals(null, response.getBody().data());
        assertEquals(null, response.getBody().error());
    }

    @Test
    public void givenResponseCodeDataAndError_whenCreateResponseWithCodeAndDataAndError_thenReturnApiResponse() {
        // Given
        ResponseCodeEn responseCode = ResponseCodeEn.SUCCESS_OPERATION;
        String data = "Sample Data";
        Map<String, List<String>> error = Map.of("field", List.of("Error message"));
        when(messageSource.getMessage(responseCode.getMessageKey(), null, LocaleContextHolder.getLocale())).thenReturn("Success message");

        // When
        ResponseEntity<ApiResponseTO<String>> response = apiResponseFactory.createResponse(responseCode, data, error);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseCode.getHttpStatus().value(), response.getBody().code());
        assertEquals(responseCode.getCode(), response.getBody().responseCode());
        assertEquals("Success message", response.getBody().message());
        assertEquals(data, response.getBody().data());
        assertEquals(error, response.getBody().error());
    }

    @Test
    public void givenResponseCode_whenCreateResponseWithNullMessage_thenReturnDefaultErrorMessage() {
        // Given
        ResponseCodeEn responseCode = ResponseCodeEn.SUCCESS_OPERATION;
        when(messageSource.getMessage(responseCode.getMessageKey(), null, LocaleContextHolder.getLocale())).thenReturn(null);

        // When
        ResponseEntity<ApiResponseTO<Object>> response = apiResponseFactory.createResponse(responseCode);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseCode.getHttpStatus().value(), response.getBody().code());
        assertEquals(responseCode.getCode(), response.getBody().responseCode());
        assertEquals("An unexpected error occurred.", response.getBody().message());
        assertEquals(null, response.getBody().data());
        assertEquals(null, response.getBody().error());
    }

    @Test
    public void givenResponseCode_whenCreateResponseWithEmptyMessage_thenReturnDefaultErrorMessage() {
        // Given
        ResponseCodeEn responseCode = ResponseCodeEn.SUCCESS_OPERATION;
        when(messageSource.getMessage(responseCode.getMessageKey(), null, LocaleContextHolder.getLocale())).thenReturn("");

        // When
        ResponseEntity<ApiResponseTO<Object>> response = apiResponseFactory.createResponse(responseCode);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseCode.getHttpStatus().value(), response.getBody().code());
        assertEquals(responseCode.getCode(), response.getBody().responseCode());
        assertEquals("An unexpected error occurred.", response.getBody().message());
        assertEquals(null, response.getBody().data());
        assertEquals(null, response.getBody().error());
    }

    @Test
    public void givenResponseCodeAndError_whenCreateResponseWithErrorAndNullData_thenReturnApiResponse() {
        // Given
        ResponseCodeEn responseCode = ResponseCodeEn.CONSTRAINT_VIOLATION;
        Map<String, List<String>> error = Map.of("field", List.of("Error message"));
        when(messageSource.getMessage(responseCode.getMessageKey(), null, LocaleContextHolder.getLocale())).thenReturn("Bad request message");

        // When
        ResponseEntity<ApiResponseTO<Object>> response = apiResponseFactory.createResponse(responseCode, LocaleContextHolder.getLocale(), null, error);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(responseCode.getHttpStatus().value(), response.getBody().code());
        assertEquals(responseCode.getCode(), response.getBody().responseCode());
        assertEquals("Bad request message", response.getBody().message());
        assertEquals(null, response.getBody().data());
        assertEquals(error, response.getBody().error());
    }

    @Test
    public void givenResponseCodeAndLocaleAndData_whenCreateResponseWithCodeAndLocaleAndData_thenReturnApiResponse() {
        // Given
        ResponseCodeEn responseCode = ResponseCodeEn.SUCCESS_OPERATION;
        String data = "Sample Data";
        Locale locale = Locale.ENGLISH;
        when(messageSource.getMessage(responseCode.getMessageKey(), null, locale)).thenReturn("Success message");

        // When
        ResponseEntity<ApiResponseTO<String>> response = apiResponseFactory.createResponse(responseCode, locale, data);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseCode.getHttpStatus().value(), response.getBody().code());
        assertEquals(responseCode.getCode(), response.getBody().responseCode());
        assertEquals("Success message", response.getBody().message());
        assertEquals(data, response.getBody().data());
        assertEquals(null, response.getBody().error());
    }
}
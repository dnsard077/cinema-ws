package com.example.cinema.cinemaws.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ValidationServiceTest {

    @Mock
    private Validator validator;

    @InjectMocks
    private ValidationService validationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void validate_givenRequestWithViolations_whenValidated_thenThrowsConstraintViolationException() {
        // Given
        Object request = new Object();
        Set<ConstraintViolation<Object>> violations = new HashSet<>();
        ConstraintViolation<Object> violation = Mockito.mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Validation error");
        violations.add(violation);

        when(validator.validate(request)).thenReturn(violations);

        // When & Then
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            validationService.validate(request);
        });

        // Verify the exception message
        assertEquals(violations, exception.getConstraintViolations());
    }

    @Test
    public void validate_givenRequestWithoutViolations_whenValidated_thenNoExceptionIsThrown() {
        // Given
        Object request = new Object();
        Set<ConstraintViolation<Object>> violations = new HashSet<>();

        when(validator.validate(request)).thenReturn(violations);

        // When
        validationService.validate(request);

        // Then no exception is thrown
    }
}
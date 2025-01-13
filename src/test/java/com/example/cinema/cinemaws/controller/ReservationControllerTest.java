package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Reservation;
import com.example.cinema.cinemaws.model.User;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ReservationControllerTest {

    @InjectMocks
    private ReservationController reservationController;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setUser(new User());
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setTotalAmount(100.0);
    }

    @Test
    public void whenGetAllReservations_thenReturnReservationList() {
        // Given
        List<Reservation> reservations = Collections.singletonList(reservation);
        when(reservationService.getAllReservations()).thenReturn(reservations);
        ApiResponseTO<List<Reservation>> expectedApiResponse = ApiResponseTO.<List<Reservation>>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(reservations)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, reservations))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<List<Reservation>>> response = reservationController.getAllReservations();

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void whenGetReservationById_thenReturnReservation() {
        // Given
        when(reservationService.getReservationById(1L)).thenReturn(reservation);
        ApiResponseTO<Reservation> expectedApiResponse = ApiResponseTO.<Reservation>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(reservation)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, reservation))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Reservation>> response = reservationController.getReservationById(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void whenCreateReservation_thenReturnCreatedReservation() {
        // Given
        when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation);
        ApiResponseTO<Reservation> expectedApiResponse = ApiResponseTO.<Reservation>builder()
                .code(ResponseCodeEn.SUCCESS_CREATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_CREATED.getCode())
                .message("success.default")
                .data(reservation)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, reservation))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Reservation>> response = reservationController.createReservation(reservation);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void whenUpdateReservation_thenReturnUpdatedReservation() {
        // Given
        when(reservationService.updateReservation(eq(1L), any(Reservation.class))).thenReturn(reservation);
        ApiResponseTO<Reservation> expectedApiResponse = ApiResponseTO.<Reservation>builder()
                .code(ResponseCodeEn.SUCCESS_UPDATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_UPDATED.getCode())
                .message("success.default")
                .data(reservation)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, reservation))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Reservation>> response = reservationController.updateReservation(1L, reservation);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void whenDeleteReservation_thenReturnSuccessResponse() {
        // Given
        doNothing().when(reservationService).deleteReservation(1L);
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_DELETED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_DELETED.getCode())
                .message("success.default")
                .data(null)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = reservationController.deleteReservation(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
        verify(reservationService, times(1)).deleteReservation(1L);
    }
}
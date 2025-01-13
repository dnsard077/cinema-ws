package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Seat;
import com.example.cinema.cinemaws.model.Studio;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SeatControllerTest {

    @InjectMocks
    private SeatController seatController;

    @Mock
    private SeatService seatService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    private Seat seat;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        seat = new Seat();
        seat.setSeatId(1L);
        seat.setSeatNumber("A1");
        seat.setIsAvailable(true);
        seat.setStudio(new Studio());
    }

    @Test
    public void givenSeatsExist_whenGetSeatsByStudioId_thenReturnSeatList() {
        // Given
        List<Seat> seats = Collections.singletonList(seat);
        when(seatService.getSeatsByStudioId(1L)).thenReturn(seats);
        ApiResponseTO<List<Seat>> expectedResponse = ApiResponseTO.<List<Seat>>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(seats)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, seats))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<List<Seat>>> response = seatController.getSeatsByStudioId(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenSeatExists_whenGetSeatById_thenReturnSeat() {
        // Given
        when(seatService.getSeatById(1L)).thenReturn(seat);
        ApiResponseTO<Seat> expectedResponse = ApiResponseTO.<Seat>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(seat)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, seat))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<Seat>> response = seatController.getSeatById(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenSeat_whenCreateSeat_thenReturnCreatedSeat() {
        // Given
        when(seatService.createSeat(any(Seat.class))).thenReturn(seat);
        ApiResponseTO<Seat> expectedResponse = ApiResponseTO.<Seat>builder()
                .code(ResponseCodeEn.SUCCESS_CREATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_CREATED.getCode())
                .message("success.default")
                .data(seat)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, seat))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<Seat>> response = seatController.createSeat(seat);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenSeatExists_whenUpdateSeat_thenReturnUpdatedSeat() {
        // Given
        when(seatService.updateSeat(eq(1L), any(Seat.class))).thenReturn(seat);
        ApiResponseTO<Seat> expectedResponse = ApiResponseTO.<Seat>builder()
                .code(ResponseCodeEn.SUCCESS_UPDATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_UPDATED.getCode())
                .message("success.default")
                .data(seat)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, seat))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<Seat>> response = seatController.updateSeat(1L, seat);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenSeatExists_whenDeleteSeat_thenReturnSuccessResponse() {
        // Given
        doNothing().when(seatService).deleteSeat(1L);
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_DELETED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_DELETED.getCode())
                .message("success.default")
                .data(null)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = seatController.deleteSeat(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
        verify(seatService, times(1)).deleteSeat(1L);
    }
}
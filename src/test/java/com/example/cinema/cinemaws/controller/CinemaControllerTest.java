package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Cinema;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.CinemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CinemaControllerTest {

    @InjectMocks
    private CinemaController cinemaController;

    @Mock
    private CinemaService cinemaService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    private Cinema cinema;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cinema = new Cinema();
        cinema.setCinemaId(1L);
        cinema.setName("Cineplex");
        cinema.setLocation("Downtown");
        cinema.setContactNumber("123-456-7890");
    }

    @Test
    public void givenValidId_whenGetCinemaById_thenReturnCinema() {
        // Given
        when(cinemaService.getCinemaById(1L)).thenReturn(cinema);
        ApiResponseTO<Cinema> expectedApiResponse = ApiResponseTO.<Cinema>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(cinema)
                .build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.SUCCESS_OPERATION), any(Cinema.class)))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Cinema>> response = cinemaController.getCinemaById(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenCinemas_whenGetAllCinemas_thenReturnCinemaPage() {
        // Given
        Page<Cinema> cinemaPage = new PageImpl<>(Collections.singletonList(cinema));
        when(cinemaService.getAllCinemas(1, 10, "cinemaId", "asc", null, null)).thenReturn(cinemaPage);
        ApiResponseTO<Page> expectedApiResponse = ApiResponseTO.<Page>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(cinemaPage)
                .build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.SUCCESS_OPERATION), any(Page.class)))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Page<Cinema>>> response = cinemaController.getAllCinemas(1, 10, "cinemaId", "asc", null, null);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenCinema_whenCreateCinema_thenReturnCreatedCinema() {
        // Given
        when(cinemaService.createCinema(any(Cinema.class))).thenReturn(cinema);
        ApiResponseTO<Cinema> expectedApiResponse = ApiResponseTO.<Cinema>builder()
                .code(ResponseCodeEn.SUCCESS_CREATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_CREATED.getCode())
                .message("success.default")
                .data(cinema)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, cinema))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Cinema>> response = cinemaController.createCinema(cinema);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenCinemaIdAndDetails_whenUpdateCinema_thenReturnUpdatedCinema() {
        // Given
        when(cinemaService.updateCinema(eq(1L), any(Cinema.class))).thenReturn(cinema);
        ApiResponseTO<Cinema> expectedApiResponse = ApiResponseTO.<Cinema>builder()
                .code(ResponseCodeEn.SUCCESS_UPDATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_UPDATED.getCode())
                .message("success.default")
                .data(cinema)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, cinema))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Cinema>> response = cinemaController.updateCinema(1L, cinema);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenCinemaId_whenDeleteCinema_thenReturnSuccessResponse() {
        // Given
        doNothing().when(cinemaService).deleteCinema(1L);
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_DELETED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_DELETED.getCode())
                .message("success.default")
                .data(null)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = cinemaController.deleteCinema(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
        verify(cinemaService, times(1)).deleteCinema(1L);
    }
}
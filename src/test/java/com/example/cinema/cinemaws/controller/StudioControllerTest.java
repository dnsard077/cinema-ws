package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Studio;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.StudioService;
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

public class StudioControllerTest {

    @InjectMocks
    private StudioController studioController;

    @Mock
    private StudioService studioService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    private Studio studio;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        studio = new Studio();
        studio.setStudioId(1L);
        studio.setStudioName("Studio 1");
        studio.setTotalSeats(100);
        // Set other properties as needed
    }

    @Test
    public void givenStudiosExist_whenGetAllStudios_thenReturnStudioList() {
        // Given
        List<Studio> studios = Collections.singletonList(studio);
        when(studioService.getAllStudios()).thenReturn(studios);
        ApiResponseTO<List<Studio>> expectedResponse = ApiResponseTO.<List<Studio>>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(studios)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, studios))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<List<Studio>>> response = studioController.getAllStudios();

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenStudioExists_whenGetStudioById_thenReturnStudio() {
        // Given
        when(studioService.getStudioById(1L)).thenReturn(studio);
        ApiResponseTO<Studio> expectedResponse = ApiResponseTO.<Studio>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(studio)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, studio))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<Studio>> response = studioController.getStudioById(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenStudio_whenCreateStudio_thenReturnCreatedStudio() {
        // Given
        when(studioService.createStudio(any(Studio.class))).thenReturn(studio);
        ApiResponseTO<Studio> expectedResponse = ApiResponseTO.<Studio>builder()
                .code(ResponseCodeEn.SUCCESS_CREATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_CREATED.getCode())
                .message("success.default")
                .data(studio)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, studio))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<Studio>> response = studioController.createStudio(studio);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenStudioExists_whenUpdateStudio_thenReturnUpdatedStudio() {
        // Given
        when(studioService.updateStudio(eq(1L), any(Studio.class))).thenReturn(studio);
        ApiResponseTO<Studio> expectedResponse = ApiResponseTO.<Studio>builder()
                .code(ResponseCodeEn.SUCCESS_UPDATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_UPDATED.getCode())
                .message("success.default")
                .data(studio)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, studio))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<Studio>> response = studioController.updateStudio(1L, studio);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void whenDeleteStudio_thenReturnSuccessResponse() {
        // Given
        doNothing().when(studioService).deleteStudio(1L);
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_DELETED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_DELETED.getCode())
                .message("success.default")
                .data(null)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = studioController.deleteStudio(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
        verify(studioService, times(1)).deleteStudio(1L);
    }
}
package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Cinema;
import com.example.cinema.cinemaws.model.Studio;
import com.example.cinema.cinemaws.repository.CinemaRepository;
import com.example.cinema.cinemaws.repository.StudioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class StudioServiceTest {

    @InjectMocks
    private StudioService studioService;

    @Mock
    private StudioRepository studioRepository;

    @Mock
    private CinemaRepository cinemaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidStudio_whenCreateStudio_thenReturnStudio() {
        // Given
        Cinema cinema = new Cinema();
        cinema.setCinemaId(1L);
        Studio studio = new Studio();
        studio.setStudioName("Studio 1");
        studio.setTotalSeats(100);
        studio.setCinema(cinema);

        when(cinemaRepository.findById(eq(1L))).thenReturn(Optional.of(cinema));
        when(studioRepository.save(any(Studio.class))).thenReturn(studio);

        // When
        Studio result = studioService.createStudio(studio);

        // Then
        assertEquals(studio, result);
        verify(studioRepository).save(studio);
    }

    @Test
    void givenInvalidCinemaId_whenCreateStudio_thenThrowException() {
        // Given
        Studio studio = new Studio();
        studio.setCinema(new Cinema());
        studio.getCinema().setCinemaId(1L);

        when(cinemaRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> studioService.createStudio(studio));
    }

    @Test
    void whenGetAllStudios_thenReturnStudioList() {
        // Given
        Studio studio = new Studio();
        studio.setStudioId(1L);
        studio.setStudioName("Studio 1");
        studio.setTotalSeats(100);
        when(studioRepository.findAll()).thenReturn(Collections.singletonList(studio));

        // When
        var result = studioService.getAllStudios();

        // Then
        assertEquals(1, result.size());
        assertEquals(studio, result.get(0));
    }

    @Test
    void givenStudioId_whenGetStudioById_thenReturnStudio() {
        // Given
        Studio studio = new Studio();
        studio.setStudioId(1L);
        when(studioRepository.findById(eq(1L))).thenReturn(Optional.of(studio));

        // When
        Studio result = studioService.getStudioById(1L);

        // Then
        assertEquals(studio, result);
    }

    @Test
    void givenInvalidStudioId_whenGetStudioById_thenThrowException() {
        // Given
        when(studioRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> studioService.getStudioById(1L));
    }

    @Test
    void givenStudioIdAndDetails_whenUpdateStudio_thenReturnUpdatedStudio() {
        // Given
        Studio existingStudio = new Studio();
        existingStudio.setStudioId(1L);
        existingStudio.setStudioName("Old Studio");
        existingStudio.setTotalSeats(50);

        Studio studioDetails = new Studio();
        studioDetails.setStudioName("Updated Studio");
        studioDetails.setTotalSeats(100);

        when(studioRepository.findById(eq(1L))).thenReturn(Optional.of(existingStudio));
        when(studioRepository.save(any(Studio.class))).thenReturn(existingStudio);

        // When
        Studio result = studioService.updateStudio(1L, studioDetails);

        // Then
        assertEquals("Updated Studio", result.getStudioName());
        assertEquals(100, result.getTotalSeats());
    }

    @Test
    void givenInvalidStudioId_whenUpdateStudio_thenThrowException() {
        // Given
        Studio studioDetails = new Studio();
        studioDetails.setStudioName("Updated Studio");
        studioDetails.setTotalSeats(100);

        when(studioRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> studioService.updateStudio(1L, studioDetails));
    }

    @Test
    void givenStudioId_whenDeleteStudio_thenStudioIsDeleted() {
        // Given
        Studio studio = new Studio();
        studio.setStudioId(1L);
        when(studioRepository.findById(eq(1L))).thenReturn(Optional.of(studio));

        // When
        studioService.deleteStudio(1L);

        // Then
        verify(studioRepository).delete(studio);
    }

    @Test
    void givenInvalidStudioId_whenDeleteStudio_thenThrowException() {
        // Given
        when(studioRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> studioService.deleteStudio(1L));
    }
}
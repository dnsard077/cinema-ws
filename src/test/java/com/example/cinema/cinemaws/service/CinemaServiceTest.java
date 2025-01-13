package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Cinema;
import com.example.cinema.cinemaws.repository.CinemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CinemaServiceTest {

    @InjectMocks
    private CinemaService cinemaService;

    @Mock
    private CinemaRepository cinemaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidCinema_whenCreateCinema_thenReturnCinema() {
        // Given
        Cinema cinema = new Cinema();
        cinema.setName("Cinema 1");
        cinema.setLocation("Location 1");
        cinema.setContactNumber("123456789");

        when(cinemaRepository.save(any(Cinema.class))).thenReturn(cinema);

        // When
        Cinema result = cinemaService.createCinema(cinema);

        // Then
        assertEquals(cinema, result);
        verify(cinemaRepository).save(cinema);
    }

    @Test
    void whenGetAllCinemas_thenReturnCinemaList() {
        // Given
        Cinema cinema = new Cinema();
        cinema.setName("Cinema 1");
        when(cinemaRepository.findAll()).thenReturn(Collections.singletonList(cinema));

        // When
        var result = cinemaService.getAllCinemas();

        // Then
        assertEquals(1, result.size());
        assertEquals(cinema, result.get(0));
    }

    @Test
    void givenCinemaId_whenGetCinemaById_thenReturnCinema() {
        // Given
        Cinema cinema = new Cinema();
        cinema.setCinemaId(1L);
        when(cinemaRepository.findById(eq(1L))).thenReturn(Optional.of(cinema));

        // When
        Cinema result = cinemaService.getCinemaById(1L);

        // Then
        assertEquals(cinema, result);
    }

    @Test
    void givenInvalidCinemaId_whenGetCinemaById_thenThrowException() {
        // Given
        when(cinemaRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> cinemaService.getCinemaById(1L));
    }

    @Test
    void givenCinemaIdAndDetails_whenUpdateCinema_thenReturnUpdatedCinema() {
        // Given
        Cinema existingCinema = new Cinema();
        existingCinema.setCinemaId(1L);
        existingCinema.setName("Old Cinema");
        existingCinema.setLocation("Old Location");
        existingCinema.setContactNumber("123456789");

        Cinema cinemaDetails = new Cinema();
        cinemaDetails.setName("Updated Cinema");
        cinemaDetails.setLocation("Updated Location");
        cinemaDetails.setContactNumber("987654321");

        when(cinemaRepository.findById(eq(1L))).thenReturn(Optional.of(existingCinema));
        when(cinemaRepository.save(any(Cinema.class))).thenReturn(existingCinema);

        // When
        Cinema result = cinemaService.updateCinema(1L, cinemaDetails);

        // Then
        assertEquals("Updated Cinema", result.getName());
        assertEquals("Updated Location", result.getLocation());
        assertEquals("987654321", result.getContactNumber());
    }

    @Test
    void givenInvalidCinemaId_whenUpdateCinema_thenThrowException() {
        // Given
        Cinema cinemaDetails = new Cinema();
        cinemaDetails.setName("Updated Cinema");

        when(cinemaRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> cinemaService.updateCinema(1L, cinemaDetails));
    }

    @Test
    void givenCinemaId_whenDeleteCinema_thenCinemaIsDeleted() {
        // Given
        Cinema cinema = new Cinema();
        cinema.setCinemaId(1L);
        when(cinemaRepository.findById(eq(1L))).thenReturn(Optional.of(cinema));

        // When
        cinemaService.deleteCinema(1L);

        // Then
        verify(cinemaRepository).delete(cinema);
    }

    @Test
    void givenInvalidCinemaId_whenDeleteCinema_thenThrowException() {
        // Given
        when(cinemaRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> cinemaService.deleteCinema(1L));
    }

    @Test
    void whenGetAllCinemasWithPagination_thenReturnPagedCinemas() {
        // Given
        Cinema cinema = new Cinema();
        cinema.setName("Cinema 1");
        Page<Cinema> pagedResponse = new PageImpl<>(Collections.singletonList(cinema));
        when(cinemaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pagedResponse);

        // When
        var result = cinemaService.getAllCinemas(1, 10, "name", "asc", null, null);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(cinema, result.getContent().get(0));
    }
}
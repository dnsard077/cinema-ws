package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Seat;
import com.example.cinema.cinemaws.model.Studio;
import com.example.cinema.cinemaws.repository.SeatRepository;
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

class SeatServiceTest {

    @InjectMocks
    private SeatService seatService;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private StudioRepository studioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidSeat_whenCreateSeat_thenReturnSeat() {
        // Given
        Studio studio = new Studio();
        studio.setStudioId(1L);
        Seat seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setIsAvailable(true);
        seat.setStudio(studio);

        when(studioRepository.findById(eq(1L))).thenReturn(Optional.of(studio));
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        // When
        Seat result = seatService.createSeat(seat);

        // Then
        assertEquals(seat, result);
        verify(seatRepository).save(seat);
    }

    @Test
    void givenInvalidStudioId_whenCreateSeat_thenThrowException() {
        // Given
        Seat seat = new Seat();
        seat.setStudio(new Studio());
        seat.getStudio().setStudioId(1L);

        when(studioRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> seatService.createSeat(seat));
    }

    @Test
    void givenStudioId_whenGetSeatsByStudioId_thenReturnSeatList() {
        // Given
        Seat seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setIsAvailable(true);
        seat.setStudio(new Studio());
        seat.getStudio().setStudioId(1L);
        when(seatRepository.findAllByStudio_StudioId(eq(1L))).thenReturn(Collections.singletonList(seat));

        // When
        var result = seatService.getSeatsByStudioId(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals(seat, result.get(0));
    }

    @Test
    void givenSeatId_whenGetSeatById_thenReturnSeat() {
        // Given
        Seat seat = new Seat();
        seat.setSeatId(1L);
        when(seatRepository.findById(eq(1L))).thenReturn(Optional.of(seat));

        // When
        Seat result = seatService.getSeatById(1L);

        // Then
        assertEquals(seat, result);
    }

    @Test
    void givenInvalidSeatId_whenGetSeatById_thenThrowException() {
        // Given
        when(seatRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> seatService.getSeatById(1L));
    }

    @Test
    void givenSeatIdAndDetails_whenUpdateSeat_thenReturnUpdatedSeat() {
        // Given
        Seat existingSeat = new Seat();
        existingSeat.setSeatId(1L);
        existingSeat.setSeatNumber("A1");
        existingSeat.setIsAvailable(true);

        Seat seatDetails = new Seat();
        seatDetails.setSeatNumber("B2");
        seatDetails.setIsAvailable(false);

        when(seatRepository.findById(eq(1L))).thenReturn(Optional.of(existingSeat));
        when(seatRepository.save(any(Seat.class))).thenReturn(existingSeat);

        // When
        Seat result = seatService.updateSeat(1L, seatDetails);

        // Then
        assertEquals("B2", result.getSeatNumber());
        assertEquals(false, result.getIsAvailable());
    }

    @Test
    void givenInvalidSeatId_whenUpdateSeat_thenThrowException() {
        // Given
        Seat seatDetails = new Seat();
        seatDetails.setSeatNumber("B2");
        seatDetails.setIsAvailable(false);

        when(seatRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> seatService.updateSeat(1L, seatDetails));
    }

    @Test
    void givenSeatId_whenDeleteSeat_thenSeatIsDeleted() {
        // Given
        Seat seat = new Seat();
        seat.setSeatId(1L);
        when(seatRepository.findById(eq(1L))).thenReturn(Optional.of(seat));

        // When
        seatService.deleteSeat(1L);

        // Then
        verify(seatRepository).delete(seat);
    }

    @Test
    void givenInvalidSeatId_whenDeleteSeat_thenThrowException() {
        // Given
        when(seatRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> seatService.deleteSeat(1L));
    }
}
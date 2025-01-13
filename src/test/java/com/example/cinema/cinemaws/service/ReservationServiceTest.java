package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Reservation;
import com.example.cinema.cinemaws.model.User;
import com.example.cinema.cinemaws.repository.ReservationRepository;
import com.example.cinema.cinemaws.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidReservation_whenCreateReservation_thenReturnReservation() {
        // Given
        User user = new User();
        user.setUserId(1L);
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setTotalAmount(100.0);

        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // When
        Reservation result = reservationService.createReservation(reservation);

        // Then
        assertEquals(reservation, result);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void givenInvalidUserId_whenCreateReservation_thenThrowException() {
        // Given
        Reservation reservation = new Reservation();
        reservation.setUser(new User());
        reservation.getUser().setUserId(1L);

        when(userRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> reservationService.createReservation(reservation));
    }

    @Test
    void whenGetAllReservations_thenReturnReservationList() {
        // Given
        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        // When
        var result = reservationService.getAllReservations();

        // Then
        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }

    @Test
    void givenReservationId_whenGetReservationById_thenReturnReservation() {
        // Given
        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.of(reservation));

        // When
        Reservation result = reservationService.getReservationById(1L);

        // Then
        assertEquals(reservation, result);
    }

    @Test
    void givenInvalidReservationId_whenGetReservationById_thenThrowException() {
        // Given
        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> reservationService.getReservationById(1L));
    }

    @Test
    void givenReservationIdAndDetails_whenUpdateReservation_thenReturnUpdatedReservation() {
        // Given
        Reservation existingReservation = new Reservation();
        existingReservation.setReservationId(1L);
        existingReservation.setReservationDate(LocalDateTime.now());
        existingReservation.setTotalAmount(100.0);
        existingReservation.setUser(new User());

        Reservation reservationDetails = new Reservation();
        reservationDetails.setReservationDate(LocalDateTime.now().plusDays(1));
        reservationDetails.setTotalAmount(150.0);
        reservationDetails.setUser(new User());

        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(existingReservation);

        // When
        Reservation result = reservationService.updateReservation(1L, reservationDetails);

        // Then
        assertEquals(reservationDetails.getReservationDate(), result.getReservationDate());
        assertEquals(reservationDetails.getTotalAmount(), result.getTotalAmount());
    }

    @Test
    void givenInvalidReservationId_whenUpdateReservation_thenThrowException() {
        // Given
        Reservation reservationDetails = new Reservation();
        reservationDetails.setUser(new User());

        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> reservationService.updateReservation(1L, reservationDetails));
    }

    @Test
    void givenReservationId_whenDeleteReservation_thenReservationIsDeleted() {
        // Given
        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.of(reservation));

        // When
        reservationService.deleteReservation(1L);

        // Then
        verify(reservationRepository).delete(reservation);
    }

    @Test
    void givenInvalidReservationId_whenDeleteReservation_thenThrowException() {
        // Given
        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> reservationService.deleteReservation(1L));
    }
}
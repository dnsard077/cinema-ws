package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Reservation;
import com.example.cinema.cinemaws.repository.ReservationRepository;
import com.example.cinema.cinemaws.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public Reservation createReservation(Reservation reservation) {
        userRepository.findById(reservation.getUser().getUserId())
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
    }

    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        existingReservation.setReservationDate(reservationDetails.getReservationDate());
        existingReservation.setUser(reservationDetails.getUser());
        existingReservation.setSchedule(reservationDetails.getSchedule());

        return reservationRepository.save(existingReservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
        reservationRepository.delete(reservation);
    }
}

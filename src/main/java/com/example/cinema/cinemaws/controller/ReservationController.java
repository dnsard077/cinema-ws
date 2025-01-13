package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Reservation;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping
    public ResponseEntity<ApiResponseTO<List<Reservation>>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Reservation>> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, reservation);
    }

    @PostMapping
    public ResponseEntity<ApiResponseTO<Reservation>> createReservation(@RequestBody Reservation reservation) {
        Reservation createdReservation = reservationService.createReservation(reservation);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, createdReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Reservation>> updateReservation(@PathVariable Long id, @RequestBody Reservation reservationDetails) {
        Reservation updatedReservation = reservationService.updateReservation(id, reservationDetails);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Object>> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED);
    }
}
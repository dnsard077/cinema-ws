package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Seat;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/seat")
public class SeatController {
    private final SeatService seatService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping("/studio/{studioId}")
    public ResponseEntity<ApiResponseTO<List<Seat>>> getSeatsByStudioId(@PathVariable Long studioId) {
        List<Seat> seats = seatService.getSeatsByStudioId(studioId);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, seats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Seat>> getSeatById(@PathVariable Long id) {
        Seat seat = seatService.getSeatById(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, seat);
    }

    @PostMapping
    public ResponseEntity<ApiResponseTO<Seat>> createSeat(@RequestBody Seat seat) {
        Seat createdSeat = seatService.createSeat(seat);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, createdSeat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Seat>> updateSeat(@PathVariable Long id, @RequestBody Seat seatDetails) {
        Seat updatedSeat = seatService.updateSeat(id, seatDetails);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, updatedSeat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Object>> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED);
    }
}
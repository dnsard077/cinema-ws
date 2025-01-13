package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Seat;
import com.example.cinema.cinemaws.repository.SeatRepository;
import com.example.cinema.cinemaws.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final StudioRepository studioRepository;

    public Seat createSeat(Seat seat) {
        studioRepository.findById(seat.getStudio().getStudioId())
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        return seatRepository.save(seat);
    }

    public List<Seat> getSeatsByStudioId(Long studioId) {
        return seatRepository.findAllByStudio_StudioId(studioId);
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
    }

    public Seat updateSeat(Long id, Seat seatDetails) {
        Seat existingSeat = seatRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        existingSeat.setSeatNumber(seatDetails.getSeatNumber());
        existingSeat.setIsAvailable(seatDetails.getIsAvailable());
        existingSeat.setStudio(seatDetails.getStudio());

        return seatRepository.save(existingSeat);
    }

    public void deleteSeat(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
        seatRepository.delete(seat);
    }
}

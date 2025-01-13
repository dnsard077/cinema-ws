package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Studio;
import com.example.cinema.cinemaws.repository.CinemaRepository;
import com.example.cinema.cinemaws.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;
    private final CinemaRepository cinemaRepository;

    public Studio createStudio(Studio studio) {
        cinemaRepository.findById(studio.getCinema().getCinemaId())
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        return studioRepository.save(studio);
    }

    public List<Studio> getAllStudios() {
        return studioRepository.findAll();
    }

    public Studio getStudioById(Long id) {
        return studioRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
    }

    public Studio updateStudio(Long id, Studio studioDetails) {
        Studio existingStudio = studioRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        existingStudio.setStudioName(studioDetails.getStudioName());
        existingStudio.setTotalSeats(studioDetails.getTotalSeats());
        existingStudio.setCinema(studioDetails.getCinema());

        return studioRepository.save(existingStudio);
    }

    public void deleteStudio(Long id) {
        Studio studio = studioRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
        studioRepository.delete(studio);
    }
}
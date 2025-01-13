package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Cinema;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cinema")
public class CinemaController {
    private final CinemaService cinemaService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping
    public ResponseEntity<ApiResponseTO<Page<Cinema>>> getAllCinemas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "cinemaId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location) {

        Page<Cinema> cinemas = cinemaService.getAllCinemas(page, size, sortBy, sortOrder, name, location);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, cinemas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Cinema>> getCinemaById(@PathVariable Long id) {
        Cinema cinema = cinemaService.getCinemaById(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, cinema);
    }

    @PostMapping
    public ResponseEntity<ApiResponseTO<Cinema>> createCinema(@RequestBody Cinema cinema) {
        Cinema createdCinema = cinemaService.createCinema(cinema);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, createdCinema);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Cinema>> updateCinema(@PathVariable Long id, @RequestBody Cinema cinema) {
        Cinema updatedCinema = cinemaService.updateCinema(id, cinema);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, updatedCinema);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Object>> deleteCinema(@PathVariable Long id) {
        cinemaService.deleteCinema(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED);
    }
}
package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.MovieMedia;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.MovieMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/movie-media")
public class MovieMediaController {

    private final MovieMediaService movieMediaService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping
    public ResponseEntity<ApiResponseTO<Page<MovieMedia>>> getAllMovieMedia(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "movieMediaId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String quality
    ) {
        Page<MovieMedia> movieMediaPage = movieMediaService.getMovieMedia(page, size, sortBy, sortOrder, quality);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, movieMediaPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTO<MovieMedia>> getMovieMediaById(@PathVariable Long id) {
        MovieMedia movieMedia = movieMediaService.getMovieMediaById(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, movieMedia);
    }

    @PostMapping(path = "/{movieId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseTO<MovieMedia>> createMovieMedia(
            @PathVariable Long movieId,
            @RequestParam("file") MultipartFile file
    ) {
        movieMediaService.createMovieMedia(movieId, file);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseTO<MovieMedia>> updateMovieMedia(
            @PathVariable Long id,
            @RequestBody MovieMedia movieMediaDetails
    ) {
        MovieMedia updatedMovieMedia = movieMediaService.updateMovieMedia(id, movieMediaDetails);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, updatedMovieMedia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Object>> deleteMovieMedia(@PathVariable Long id) {
        movieMediaService.deleteMovieMedia(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED);
    }
}

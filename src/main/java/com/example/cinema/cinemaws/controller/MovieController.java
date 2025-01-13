package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/movie")
public class MovieController {
    private final MovieService movieService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping
    public ResponseEntity<ApiResponseTO<Page<Movie>>> getAllMovies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "movieId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre
    ) {
        Page<Movie> movies = movieService.getAllMovies(page, size, sortBy, sortOrder, title, genre);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Movie>> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, movie);
    }

    @PostMapping
    public ResponseEntity<ApiResponseTO<Movie>> createMovie(@RequestBody Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, createdMovie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Movie>> updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
        Movie updatedMovie = movieService.updateMovie(id, movieDetails);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, updatedMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Object>> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED);
    }
}
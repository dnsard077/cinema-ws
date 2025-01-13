package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    private Movie movie;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        movie = new Movie();
        movie.setMovieId(1L);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setDuration(148);
    }

    @Test
    public void givenMovies_whenGetAllMovies_thenReturnMoviePage() {
        // Given
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));
        when(movieService.getAllMovies(1, 10, "movieId", "asc", null, null)).thenReturn(moviePage);
        ApiResponseTO<Page> expectedApiResponse = ApiResponseTO.<Page>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(moviePage)
                .build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.SUCCESS_OPERATION), any(Page.class)))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Page<Movie>>> response = movieController.getAllMovies(1, 10, "movieId", "asc", null, null);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenValidId_whenGetMovieById_thenReturnMovie() {
        // Given
        when(movieService.getMovieById(1L)).thenReturn(movie);
        ApiResponseTO<Movie> expectedApiResponse = ApiResponseTO.<Movie>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(movie)
                .build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.SUCCESS_OPERATION), any(Movie.class)))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Movie>> response = movieController.getMovieById(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenMovie_whenCreateMovie_thenReturnCreatedMovie() {
        // Given
        when(movieService.createMovie(any(Movie.class))).thenReturn(movie);
        ApiResponseTO<Movie> expectedApiResponse = ApiResponseTO.<Movie>builder()
                .code(ResponseCodeEn.SUCCESS_CREATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_CREATED.getCode())
                .message("success.default")
                .data(movie)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, movie))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Movie>> response = movieController.createMovie(movie);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenMovieIdAndDetails_whenUpdateMovie_thenReturnUpdatedMovie() {
        // Given
        when(movieService.updateMovie(eq(1L), any(Movie.class))).thenReturn(movie);
        ApiResponseTO<Movie> expectedApiResponse = ApiResponseTO.<Movie>builder()
                .code(ResponseCodeEn.SUCCESS_UPDATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_UPDATED.getCode())
                .message("success.default")
                .data (movie)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, movie))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Movie>> response = movieController.updateMovie(1L, movie);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenMovieId_whenDeleteMovie_thenReturnSuccessResponse() {
        // Given
        doNothing().when(movieService).deleteMovie(1L);
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_DELETED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_DELETED.getCode())
                .message("success.default")
                .data(null)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = movieController.deleteMovie(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
        verify(movieService, times(1)).deleteMovie(1L);
    }
}
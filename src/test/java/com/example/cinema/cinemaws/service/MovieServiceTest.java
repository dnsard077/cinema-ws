package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidMovie_whenCreateMovie_thenReturnMovie() {
        // Given
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setDuration(148);
        movie.setReleaseDate(LocalDate.of(2010, 7, 16).atStartOfDay());

        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // When
        Movie result = movieService.createMovie(movie);

        // Then
        assertEquals(movie, result);
        verify(movieRepository).save(movie);
    }

    @Test
    void whenGetAllMovies_thenReturnMovieList() {
        // Given
        Movie movie = new Movie();
        movie.setTitle("Inception");
        when(movieRepository.findAll()).thenReturn(Collections.singletonList(movie));

        // When
        var result = movieService.getAllMovies();

        // Then
        assertEquals(1, result.size());
        assertEquals(movie, result.get(0));
    }

    @Test
    void whenGetAllMoviesWithPagination_thenReturnPagedMovies() {
        // Given
        Movie movie = new Movie();
        movie.setTitle("Inception");
        Page<Movie> pagedResponse = new PageImpl<>(Collections.singletonList(movie));
        when(movieRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pagedResponse);

        // When
        Page<Movie> result = movieService.getAllMovies(1, 10, "title", "asc", null, null);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(movie, result.getContent().get(0));
    }

    @Test
    void givenMovieId_whenGetMovieById_thenReturnMovie() {
        // Given
        Movie movie = new Movie();
        movie.setMovieId(1L);
        when(movieRepository.findById(eq(1L))).thenReturn(Optional.of(movie));

        // When
        Movie result = movieService.getMovieById(1L);

        // Then
        assertEquals(movie, result);
    }

    @Test
    void givenInvalidMovieId_whenGetMovieById_thenThrowException() {
        // Given
        when(movieRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> movieService.getMovieById(1L));
    }

    @Test
    void givenMovieIdAndDetails_whenUpdateMovie_thenReturnUpdatedMovie() {
        // Given
        Movie existingMovie = new Movie();
        existingMovie.setMovieId(1L);
        existingMovie.setTitle("Old Title");
        existingMovie.setGenre("Old Genre");
        existingMovie.setDuration(120);
        existingMovie.setReleaseDate(LocalDate.of(2000, 1, 1).atStartOfDay());

        Movie movieDetails = new Movie();
        movieDetails.setTitle("Updated Title");
        movieDetails.setGenre("Updated Genre");
        movieDetails.setDuration(150);
        movieDetails.setReleaseDate(LocalDate.of(2022, 1, 1).atStartOfDay());

        when(movieRepository.findById(eq(1L))).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(existingMovie);

        // When
        Movie result = movieService.updateMovie(1L, movieDetails);

        // Then
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Genre", result.getGenre());
        assertEquals(150, result.getDuration());
        assertEquals(LocalDate.of(2022, 1, 1).atStartOfDay(), result.getReleaseDate());
    }

    @Test
    void givenInvalidMovieId_whenUpdateMovie_thenThrowException() {
        // Given
        Movie movieDetails = new Movie();
        movieDetails.setTitle("Updated Title");

        when(movieRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> movieService.updateMovie(1L, movieDetails));
    }

    @Test
    void givenMovieId_whenDeleteMovie_thenMovieIsDeleted() {
        // Given
        Movie movie = new Movie();
        movie.setMovieId(1L);
        when(movieRepository.findById(eq(1L))).thenReturn(Optional.of(movie));

        // When
        movieService.deleteMovie(1L);

        // Then
        verify(movieRepository).delete(movie);
    }

    @Test
    void givenInvalidMovieId_whenDeleteMovie_thenThrowException() {
        // Given
        when(movieRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> movieService.deleteMovie(1L));
    }
}
package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.FileUploadTO;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.model.MovieMedia;
import com.example.cinema.cinemaws.repository.MovieMediaRepository;
import com.example.cinema.cinemaws.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MovieMediaServiceTest {

    @InjectMocks
    private MovieMediaService movieMediaService;

    @Mock
    private MovieMediaRepository movieMediaRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidMovieIdAndFile_whenCreateMovieMedia_thenReturnMovieMedia() {
        // Given
        Long movieId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setMovieId(movieId);
        when(movieRepository.findById(eq(movieId))).thenReturn(Optional.of(movie));
        when(file.getOriginalFilename()).thenReturn("testfile.mp4");
        when(file.getSize()).thenReturn(1024L);
        when(movieMediaRepository.save(any(MovieMedia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        movieMediaService.createMovieMedia(movieId, file);

        // Then
        verify(movieRepository).findById(movieId);
        verify(fileService).uploadFile(any(FileUploadTO.class));
        verify(movieMediaRepository).save(any(MovieMedia.class));
    }

    @Test
    void whenGetAllMovieMedia_thenReturnMovieMediaList() {
        // Given
        MovieMedia movieMedia = new MovieMedia();
        when(movieMediaRepository.findAll()).thenReturn(Collections.singletonList(movieMedia));

        // When
        var result = movieMediaService.getAllMovieMedia();

        // Then
        assertEquals(1, result.size());
        assertEquals(movieMedia, result.get(0));
    }

    @Test
    void givenValidId_whenGetMovieMediaById_thenReturnMovieMedia() {
        // Given
        Long id = 1L;
        MovieMedia movieMedia = new MovieMedia();
        movieMedia.setMovieMediaId(id);
        when(movieMediaRepository.findById(eq(id))).thenReturn(Optional.of(movieMedia));

        // When
        MovieMedia result = movieMediaService.getMovieMediaById(id);

        // Then
        assertEquals(movieMedia, result);
    }

    @Test
    void givenInvalidId_whenGetMovieMediaById_thenThrowException() {
        // Given
        Long id = 1L;
        when(movieMediaRepository.findById(eq(id))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> movieMediaService.getMovieMediaById(id));
    }

    @Test
    void givenPageDetails_whenGetMovieMedia_thenReturnPagedMovieMedia() {
        // Given
        int page = 1;
        int size = 10;
        String sortBy = "fileName";
        String sortOrder = "asc";
        String quality = "4K";
        MovieMedia movieMedia = new MovieMedia();
        movieMedia.setQuality("4K");

        Page<MovieMedia> pagedResponse = new PageImpl<>(Collections.singletonList(movieMedia));
        Pageable expectedPageable = PageRequest.of(0, size, Sort.by(sortBy));
        when(movieMediaRepository.findAll(any(Specification.class), eq(expectedPageable))).thenReturn(pagedResponse);

        // When
        var result = movieMediaService.getMovieMedia(page, size, sortBy, sortOrder, quality);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(movieMedia, result.getContent().get(0));
    }


    @Test
    void givenValidIdAndDetails_whenUpdateMovieMedia_thenReturnUpdatedMovieMedia() {
        // Given
        Long id = 1L;
        MovieMedia existingMedia = new MovieMedia();
        existingMedia.setMovieMediaId(id);
        MovieMedia updatedMedia = new MovieMedia();
        updatedMedia.setQuality("HD");
        when(movieMediaRepository.findById(eq(id))).thenReturn(Optional.of(existingMedia));
        when(movieMediaRepository.save(any(MovieMedia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        MovieMedia result = movieMediaService.updateMovieMedia(id, updatedMedia);

        // Then
        assertEquals("HD", result.getQuality());
        verify(movieMediaRepository).save(existingMedia);
    }

    @Test
    void givenInvalidId_whenUpdateMovieMedia_thenThrowException() {
        // Given
        Long id = 1L;
        MovieMedia updatedMedia = new MovieMedia();
        when(movieMediaRepository.findById(eq(id))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> movieMediaService.updateMovieMedia(id, updatedMedia));
    }

    @Test
    void givenValidId_whenDeleteMovieMedia_thenMovieMediaIsDeleted() {
        // Given
        Long id = 1L;
        MovieMedia movieMedia = new MovieMedia();
        movieMedia.setMovieMediaId(id);
        when(movieMediaRepository.findById(eq(id))).thenReturn(Optional.of(movieMedia));

        // When
        movieMediaService.deleteMovieMedia(id);

        // Then
        verify(movieMediaRepository).delete(movieMedia);
    }

    @Test
    void givenInvalidId_whenDeleteMovieMedia_thenThrowException() {
        // Given
        Long id = 1L;
        when(movieMediaRepository.findById(eq(id))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> movieMediaService.deleteMovieMedia(id));
    }
}

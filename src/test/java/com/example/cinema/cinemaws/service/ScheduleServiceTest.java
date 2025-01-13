package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.model.Schedule;
import com.example.cinema.cinemaws.model.Studio;
import com.example.cinema.cinemaws.repository.ScheduleRepository;
import com.example.cinema.cinemaws.repository.MovieRepository;
import com.example.cinema.cinemaws.repository.StudioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private StudioRepository studioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidSchedule_whenCreateSchedule_thenReturnSchedule() {
        // Given
        Movie movie = new Movie();
        movie.setMovieId(1L);
        Studio studio = new Studio();
        studio.setStudioId(1L);
        Schedule schedule = new Schedule();
        schedule.setMovie(movie);
        schedule.setStudio(studio);
        schedule.setStartTime(LocalDateTime.now());
        schedule.setEndTime(LocalDateTime.now().plusHours(2));

        when(movieRepository.findById(eq(1L))).thenReturn(Optional.of(movie));
        when(studioRepository.findById(eq(1L))).thenReturn(Optional.of(studio));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        // When
        Schedule result = scheduleService.createSchedule(schedule);

        // Then
        assertEquals(schedule, result);
        verify(scheduleRepository).save(schedule);
    }

    @Test
    void givenInvalidMovieId_whenCreateSchedule_thenThrowException() {
        // Given
        Studio studio = new Studio();
        studio.setStudioId(1L);
        Schedule schedule = new Schedule();
        schedule.setMovie(new Movie());
        schedule.getMovie().setMovieId(1L);
        schedule.setStudio(studio);

        when(movieRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> scheduleService.createSchedule(schedule));
    }

    @Test
    void givenInvalidStudioId_whenCreateSchedule_thenThrowException() {
        // Given
        Movie movie = new Movie();
        movie.setMovieId(1L);
        Schedule schedule = new Schedule();
        schedule.setMovie(movie);
        schedule.setStudio(new Studio());
        schedule.getStudio().setStudioId(1L);

        when(movieRepository.findById(eq(1L))).thenReturn(Optional.of(movie));
        when(studioRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> scheduleService.createSchedule(schedule));
    }

    @Test
    void whenGetAllSchedules_thenReturnScheduleList() {
        // Given
        Schedule schedule = new Schedule();
        schedule.setScheduleId(1L);
        when(scheduleRepository.findAll()).thenReturn(Collections.singletonList(schedule));

        // When
        var result = scheduleService.getAllSchedules();

        // Then
        assertEquals(1, result.size());
        assertEquals(schedule, result.get(0));
    }

    @Test
    void givenScheduleId_whenGetScheduleById_thenReturnSchedule() {
        // Given
        Schedule schedule = new Schedule();
        schedule.setScheduleId(1L);
        when(scheduleRepository.findById(eq(1L))).thenReturn(Optional.of(schedule));

        // When
        Schedule result = scheduleService.getScheduleById(1L);

        // Then
        assertEquals(schedule, result);
    }

    @Test
    void givenInvalidScheduleId_whenGetScheduleById_thenThrowException() {
        // Given
        when(scheduleRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> scheduleService.getScheduleById(1L));
    }

    @ Test
    void givenScheduleIdAndDetails_whenUpdateSchedule_thenReturnUpdatedSchedule() {
        // Given
        Schedule existingSchedule = new Schedule();
        existingSchedule.setScheduleId(1L);
        existingSchedule.setMovie(new Movie());
        existingSchedule.setStudio(new Studio());
        existingSchedule.setStartTime(LocalDateTime.now());
        existingSchedule.setEndTime(LocalDateTime.now().plusHours(2));

        Schedule scheduleDetails = new Schedule();
        scheduleDetails.setMovie(new Movie());
        scheduleDetails.setStudio(new Studio());
        scheduleDetails.setStartTime(LocalDateTime.now().plusHours(1));
        scheduleDetails.setEndTime(LocalDateTime.now().plusHours(3));

        when(scheduleRepository.findById(eq(1L))).thenReturn(Optional.of(existingSchedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(existingSchedule);

        // When
        Schedule result = scheduleService.updateSchedule(1L, scheduleDetails);

        // Then
        assertEquals(scheduleDetails.getStartTime(), result.getStartTime());
        assertEquals(scheduleDetails.getEndTime(), result.getEndTime());
    }

    @Test
    void givenInvalidScheduleId_whenUpdateSchedule_thenThrowException() {
        // Given
        Schedule scheduleDetails = new Schedule();
        scheduleDetails.setMovie(new Movie());
        scheduleDetails.setStudio(new Studio());

        when(scheduleRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> scheduleService.updateSchedule(1L, scheduleDetails));
    }

    @Test
    void givenScheduleId_whenDeleteSchedule_thenScheduleIsDeleted() {
        // Given
        Schedule schedule = new Schedule();
        schedule.setScheduleId(1L);
        when(scheduleRepository.findById(eq(1L))).thenReturn(Optional.of(schedule));

        // When
        scheduleService.deleteSchedule(1L);

        // Then
        verify(scheduleRepository).delete(schedule);
    }

    @Test
    void givenInvalidScheduleId_whenDeleteSchedule_thenThrowException() {
        // Given
        when(scheduleRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> scheduleService.deleteSchedule(1L));
    }
}
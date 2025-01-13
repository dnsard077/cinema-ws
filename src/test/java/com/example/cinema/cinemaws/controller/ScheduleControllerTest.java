package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.model.Schedule;
import com.example.cinema.cinemaws.model.Studio;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ScheduleControllerTest {

    @InjectMocks
    private ScheduleController scheduleController;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    private Schedule schedule;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        schedule = new Schedule();
        schedule.setScheduleId(1L);
        schedule.setMovie(new Movie());
        schedule.setStudio(new Studio());
        schedule.setStartTime(LocalDateTime.now());
        schedule.setEndTime(LocalDateTime.now().plusHours(2));
        schedule.setPrice(10.0);
    }

    @Test
    public void givenSchedulesExist_whenGetAllSchedules_thenReturnScheduleList() {
        // Given
        List<Schedule> schedules = Collections.singletonList(schedule);
        when(scheduleService.getAllSchedules()).thenReturn(schedules);
        ApiResponseTO<List<Schedule>> expectedResponse = ApiResponseTO.<List<Schedule>>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(schedules)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, schedules))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<List<Schedule>>> response = scheduleController.getAllSchedules();

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenScheduleExists_whenGetScheduleById_thenReturnSchedule() {
        // Given
        when(scheduleService.getScheduleById(1L)).thenReturn(schedule);
        ApiResponseTO<Schedule> expectedResponse = ApiResponseTO.<Schedule>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(schedule)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, schedule))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<Schedule>> response = scheduleController.getScheduleById(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenSchedule_whenCreateSchedule_thenReturnCreatedSchedule() {
        // Given
        when(scheduleService.createSchedule(any(Schedule.class))).thenReturn(schedule);
        ApiResponseTO<Schedule> expectedResponse = ApiResponseTO.<Schedule>builder()
                .code(ResponseCodeEn.SUCCESS_CREATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_CREATED.getCode())
                .message("success.default")
                .data(schedule)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, schedule))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<Schedule>> response = scheduleController.createSchedule(schedule);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenScheduleExists_whenUpdateSchedule_thenReturnUpdatedSchedule() {
        // Given
        when(scheduleService.updateSchedule(eq(1L), any(Schedule.class))).thenReturn(schedule);
        ApiResponseTO<Schedule> expectedResponse = ApiResponseTO.<Schedule>builder()
                .code(ResponseCodeEn.SUCCESS_UPDATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_UPDATED.getCode())
                .message("success.default")
                .data(schedule)
                .build
                        ();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, schedule))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        ResponseEntity<ApiResponseTO<Schedule>> response = scheduleController.updateSchedule(1L, schedule);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), response);
    }

    @Test
    public void givenScheduleExists_whenDeleteSchedule_thenReturnSuccessResponse() {
        // Given
        doNothing().when(scheduleService).deleteSchedule(1L);
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_DELETED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_DELETED.getCode())
                .message("success.default")
                .data(null)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = scheduleController.deleteSchedule(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
        verify(scheduleService, times(1)).deleteSchedule(1L);
    }
}
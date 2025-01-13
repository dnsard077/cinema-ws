package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Schedule;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping
    public ResponseEntity<ApiResponseTO<List<Schedule>>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Schedule>> getScheduleById(@PathVariable Long id) {
        Schedule schedule = scheduleService.getScheduleById(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, schedule);
    }

    @PostMapping
    public ResponseEntity<ApiResponseTO<Schedule>> createSchedule(@RequestBody Schedule schedule) {
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, createdSchedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Schedule>> updateSchedule(@PathVariable Long id, @RequestBody Schedule scheduleDetails) {
        Schedule updatedSchedule = scheduleService.updateSchedule(id, scheduleDetails);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, updatedSchedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Object>> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED);
    }
}
package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.Studio;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/studio")
public class StudioController {
    private final StudioService studioService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping
    public ResponseEntity<ApiResponseTO<List<Studio>>> getAllStudios() {
        List<Studio> studios = studioService.getAllStudios();
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, studios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Studio>> getStudioById(@PathVariable Long id) {
        Studio studio = studioService.getStudioById(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, studio);
    }

    @PostMapping
    public ResponseEntity<ApiResponseTO<Studio>> createStudio(@RequestBody Studio studio) {
        Studio createdStudio = studioService.createStudio(studio);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, createdStudio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Studio>> updateStudio(@PathVariable Long id, @RequestBody Studio studioDetails) {
        Studio updatedStudio = studioService.updateStudio(id, studioDetails);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, updatedStudio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Object>> deleteStudio(@PathVariable Long id) {
        studioService.deleteStudio(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED);
    }
}
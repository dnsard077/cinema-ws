package com.example.cinema.cinemaws.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ApiResponseTO<T>(
        int code,
        String responseCode,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T data,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Map<String, List<String>> error
) {
}
package com.example.cinema.cinemaws.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginReqTO(
        @NotBlank(message = "{validation.username.mandatory}") String username,
        @NotBlank(message = "{validation.password.mandatory}") String password
) {
}
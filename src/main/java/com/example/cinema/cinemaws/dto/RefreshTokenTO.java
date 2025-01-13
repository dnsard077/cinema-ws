package com.example.cinema.cinemaws.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenTO(@NotBlank(message = "{validation.refresh.token.mandatory}") String refreshToken) {
}

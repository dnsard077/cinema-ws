package com.example.cinema.cinemaws.dto;

import com.example.cinema.cinemaws.validation.UniqueEmail;
import com.example.cinema.cinemaws.validation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationTO(
        @NotBlank(message = "{validation.username.mandatory}")
        @UniqueUsername
        String username,

        @NotBlank(message = "{validation.email.mandatory}")
        @Email(message = "{validation.email.invalid}")
        @UniqueEmail
        String email,

        @NotBlank(message = "{validation.password.mandatory}")
        @Size(min = 6, message = "{validation.password.size}")
        String password
) {
}
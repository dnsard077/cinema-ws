package com.example.cinema.cinemaws.dto;

import com.example.cinema.cinemaws.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record AccessTokenTO(Long userId, String username, String email, User.Role role) {
}

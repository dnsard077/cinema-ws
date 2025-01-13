package com.example.cinema.cinemaws.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record EmailReqTO(
        @NotBlank(message = "Recipient email address is required")
        @Email(message = "Invalid email format")
        String to,
        List<String> cc,
        List<String> bcc,

        @NotBlank(message = "Subject is required")
        String subject,

        @NotBlank(message = "Content is required")
        String content,

        List<String> attachments,

        String from,

        @Email(message = "Invalid email format")
        String replyTo,
        String contentType,

        @Size(max = 10, message = "Priority must be 10 characters or less")
        String priority

) {
    public EmailReqTO {
        if (from == null || from.isBlank()) {
            from = "hello@examplecinema.com";
        }
    }
}

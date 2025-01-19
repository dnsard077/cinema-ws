package com.example.cinema.cinemaws.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record FileUploadTO(String fileName, String filePath, MultipartFile file) {
}

package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/report")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/user")
    public ResponseEntity<byte[]> exportUser() throws IOException {
        ByteArrayInputStream excelFile = reportService.exportUserToExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile.readAllBytes());
    }
}

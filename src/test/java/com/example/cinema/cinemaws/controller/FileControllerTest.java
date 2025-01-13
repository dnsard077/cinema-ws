package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FileControllerTest {

    @InjectMocks
    private FileController fileController;

    @Mock
    private FileService fileService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidFile_whenUploadFile_thenReturnSuccessResponse() throws IOException {
        // Given
        String fileName = "testFile.txt";
        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
        when(file.getSize()).thenReturn(12L);
        when(file.getContentType()).thenReturn("text/plain");

        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(null)
                .build();

        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION)).thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = fileController.uploadFile(file);

        // Then
        verify(fileService).uploadFile(file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedApiResponse, response.getBody());
    }

    @Test
    void givenFileName_whenDownloadFile_thenReturnFileData() {
        // Given
        String fileName = "testFile.txt";
        byte[] fileData = "test content".getBytes();
        when(fileService.downloadFile(fileName)).thenReturn(fileData);

        // When
        ResponseEntity<byte[]> response = fileController.downloadFile(fileName);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileData, response.getBody());
        assertEquals("attachment; filename=\"testFile.txt\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    void whenListFiles_thenReturnFileList() {
        // Given
        List<String> fileList = Collections.singletonList("testFile.txt");
        when(fileService.listFiles()).thenReturn(fileList);

        ApiResponseTO<List<String>> expectedApiResponse = ApiResponseTO.<List<String>>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(fileList)
                .build();

        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, fileList)).thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<List<String>>> response = fileController.listFiles();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedApiResponse, response.getBody());
    }

    @Test
    void givenFileName_whenDeleteFile_thenReturnSuccessResponse() {
        // Given
        String fileName = "testFile.txt";
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_DELETED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_DELETED.getCode())
                .message("success.default")
                .data(null)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED)).thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = fileController.deleteFile(fileName);

        // Then
        verify(fileService).deleteFile(fileName);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
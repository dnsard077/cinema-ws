package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.FileUploadTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3TransferManager s3TransferManager;

    @InjectMocks
    private FileService fileService;

    private String bucketName = "test-bucket";

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        fileService = new FileService(s3Client, s3TransferManager);
        Field field = FileService.class.getDeclaredField("bucketName");
        field.setAccessible(true);
        field.set(fileService, bucketName);
    }

    @Test
    void givenValidFileUploadTO_whenUploadFile_thenReturnFileName() throws Exception {
        // Given
        String fileName = "testFile.txt";
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        FileUploadTO fileUploadTO = mock(FileUploadTO.class);
        MockMultipartFile mockFile = new MockMultipartFile(fileName, inputStream);

        when(fileUploadTO.fileName()).thenReturn(fileName);
        when(fileUploadTO.file()).thenReturn(mockFile);
        when(fileUploadTO.filePath()).thenReturn("uploads/");

        // When
        String result = fileService.uploadFile(fileUploadTO);

        // Then
        assertNotNull(result);
        assertTrue(result.startsWith(fileName));
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void givenNonExistentFile_whenDownloadFile_thenThrowException() {
        // Given
        String fileName = "nonExistentFile.txt";
        when(s3Client.getObject(any(GetObjectRequest.class), any(ResponseTransformer.class)))
                .thenThrow(NoSuchKeyException.class);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileService.downloadFile(fileName));
        assertEquals("File not found: " + fileName, exception.getMessage());
    }

    @Test
    void givenFilesInBucket_whenListFiles_thenReturnFileList() {
        // Given
        S3Object s3Object = S3Object.builder().key("file1.txt").build();
        ListObjectsV2Response response = ListObjectsV2Response.builder()
                .contents(Collections.singletonList(s3Object))
                .build();
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(response);

        // When
        List<String> result = fileService.listFiles();

        // Then
        assertEquals(1, result.size());
        assertEquals("file1.txt", result.get(0));
    }

    @Test
    void givenValidFileName_whenDeleteFile_thenNoException() {
        // Given
        String fileName = "fileToDelete.txt";

        // When
        fileService.deleteFile(fileName);

        // Then
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

}
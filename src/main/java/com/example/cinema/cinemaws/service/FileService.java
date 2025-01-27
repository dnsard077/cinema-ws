package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.FileUploadResTO;
import com.example.cinema.cinemaws.dto.FileUploadTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final S3Client s3Client;
    private final S3TransferManager s3TransferManager;
    private final long partSize = 5 * 1024 * 1024;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(FileUploadTO fileUploadTO) {
        String fileName = fileUploadTO.fileName() != null ? fileUploadTO.fileName() : UUID.randomUUID() + "_" + fileUploadTO.file().getOriginalFilename();
        String key = fileUploadTO.filePath() + fileName;
        try (InputStream inputStream = fileUploadTO.file().getInputStream()) {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(fileUploadTO.file().getContentType())
                            .build(),
                    RequestBody.fromInputStream(inputStream, fileUploadTO.file().getSize()));
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    public byte[] downloadFile(String fileName) {
        try {
            return s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build(), ResponseTransformer.toBytes()).asByteArray();
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("File not found: " + fileName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file: " + e.getMessage(), e);
        }
    }

    public List<String> listFiles() {
        try {
            return s3Client.listObjectsV2(ListObjectsV2Request.builder()
                            .bucket(bucketName)
                            .build())
                    .contents()
                    .stream()
                    .map(S3Object::key)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to list files: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    public FileUploadResTO uploadFileMultipart(FileUploadTO fileUploadTO) {
        String fileName = fileUploadTO.fileName() != null ? fileUploadTO.fileName() : fileUploadTO.file().getOriginalFilename();
        String finalName = fileName + "_" + UUID.randomUUID();
        String key = fileUploadTO.filePath() + finalName;
        String uploadId = initiateMultipartUpload(key);

        try (InputStream inputStream = fileUploadTO.file().getInputStream()) {
            List<CompletedPart> completedParts = uploadParts(inputStream, key, uploadId);
            completeMultipartUpload(key, uploadId, completedParts);
            return new FileUploadResTO(finalName, key);
        } catch (Exception e) {
            abortMultipartUpload(key, uploadId);
            throw new RuntimeException("Multipart upload failed: " + e.getMessage(), e);
        }
    }

    private String initiateMultipartUpload(String key) {
        CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        CreateMultipartUploadResponse response = s3Client.createMultipartUpload(createRequest);
        return response.uploadId();
    }

    private List<CompletedPart> uploadParts(InputStream inputStream, String key, String uploadId) throws IOException {
        List<CompletedPart> completedParts = new ArrayList<>();
        byte[] buffer = new byte[(int) partSize];
        int bytesRead, partNumber = 1;

        while ((bytesRead = inputStream.read(buffer)) > 0) {
            byte[] partData = (bytesRead < buffer.length) ? java.util.Arrays.copyOf(buffer, bytesRead) : buffer;

            UploadPartRequest uploadRequest = UploadPartRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .uploadId(uploadId)
                    .partNumber(partNumber)
                    .build();

            UploadPartResponse response = s3Client.uploadPart(uploadRequest, RequestBody.fromBytes(partData));
            completedParts.add(CompletedPart.builder()
                    .partNumber(partNumber)
                    .eTag(response.eTag())
                    .build());

            partNumber++;
        }

        return completedParts;
    }

    private String completeMultipartUpload(String key, String uploadId, List<CompletedPart> completedParts) {
        CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                .parts(completedParts)
                .build();

        CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(key)
                .uploadId(uploadId)
                .multipartUpload(completedMultipartUpload)
                .build();

        CompleteMultipartUploadResponse response = s3Client.completeMultipartUpload(completeRequest);
        return response.location();
    }

    private void abortMultipartUpload(String key, String uploadId) {
        AbortMultipartUploadRequest abortRequest = AbortMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(key)
                .uploadId(uploadId)
                .build();

        s3Client.abortMultipartUpload(abortRequest);
    }
}
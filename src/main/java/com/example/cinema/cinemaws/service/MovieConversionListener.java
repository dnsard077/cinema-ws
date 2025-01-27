package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.model.MovieMedia;
import com.example.cinema.cinemaws.repository.MovieMediaRepository;
import com.example.cinema.cinemaws.repository.MovieRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MovieConversionListener {

    private final ObjectMapper objectMapper;
    private final FileService fileService;
    private final MovieRepository movieRepository;
    private final MovieMediaRepository movieMediaRepository;
    private final SqsClient sqsClient;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${sqs.queue.url}")
    private String sqsQueueUrl;

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Value("${ffprobe.path}")
    private String ffprobePath;

    @Scheduled(fixedDelay = 5000)
    public void listen() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(sqsQueueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        ReceiveMessageResponse response = sqsClient.receiveMessage(receiveMessageRequest);

        List<Message> messages = response.messages();
        if (!messages.isEmpty()) {
            for (Message message : messages) {
                try {
                    JsonNode json = objectMapper.readTree(message.body());
                    String fileName = json.get("fileName").asText();
                    String filePath = json.get("filePath").asText();
                    Long movieId = json.get("movieId").asLong();

                    byte[] tempFile = fileService.downloadFile(filePath + fileName);
                    convertAndSaveMovieMedia(tempFile, fileName, movieId);

                } catch (Exception e) {
                    log.error("Failed to process message: " + message.body(), e);
                }

                deleteMessage(message);
            }
        }
    }

    private void deleteMessage(Message message) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(sqsQueueUrl)
                .receiptHandle(message.receiptHandle())
                .build();

        try {
            sqsClient.deleteMessage(deleteMessageRequest);
            log.info("Deleted message: " + message.messageId());
        } catch (SqsException e) {
            log.error("Failed to delete message: " + message.messageId(), e);
        }
    }

    private File createTempFile(byte[] fileContent, String fileName) throws IOException {
        String resourcePath = new File(getClass().getClassLoader().getResource("").getPath(), "movie").getAbsolutePath();

        File resourceDir = new File(resourcePath);
        if (!resourceDir.exists()) {
            resourceDir.mkdirs();
        }

        File tempFile = new File(resourcePath, fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(fileContent);
        }

        return tempFile;
    }

    private void uploadToS3(File file, String movieId, String resolution) {
        try {
            String s3Path = "movie/" + movieId + "/" + resolution + "/" + file.getName();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Path)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            log.info("File uploaded to S3: " + s3Path);
        } catch (Exception e) {
            log.error("Failed to upload file to S3: " + file.getName(), e);
            throw new RuntimeException("Failed to upload to S3", e);
        }
    }

    private void convertAndSaveMovieMedia(byte[] inputFile, String fileName, Long movieId) {
        String[] resolutions = {"144p", "360p", "480p", "720p", "1080p", "2k", "4k"};
        String[] resolutionDimensions = {"256x144", "640x360", "854x480", "1280x720", "1920x1080", "2048x1080", "3840x2160"};

        for (int i = 0; i < resolutions.length; i++) {
            try {
                File tempInputFile = createTempFile(inputFile, fileName);

                String resolutionDir = "converted" + File.separator + movieId + File.separator + resolutions[i];
                File resourceDir = new File(resolutionDir);
                if (!resourceDir.exists()) {
                    resourceDir.mkdirs();
                }

                File outputFile = new File(resourceDir, fileName);

                FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
                FFprobe ffprobe = new FFprobe(ffprobePath);
                FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
                FFmpegBuilder builder = new FFmpegBuilder()
                        .setInput(tempInputFile.getAbsolutePath())
                        .addOutput(outputFile.getAbsolutePath())
                        .setVideoResolution(
                                Integer.parseInt(resolutionDimensions[i].split("x")[0]),
                                Integer.parseInt(resolutionDimensions[i].split("x")[1])
                        )
                        .done();

                executor.createJob(builder).run();

                uploadToS3(outputFile, movieId.toString(), resolutions[i]);

                Movie movie = movieRepository.findById(movieId)
                        .orElseThrow(() -> new RuntimeException("Movie not found"));

                MovieMedia movieMedia = new MovieMedia();
                movieMedia.setMovie(movie);
                movieMedia.setFileName(fileName);
                movieMedia.setFileSize(outputFile.length());
                movieMedia.setQuality(resolutions[i]);
                movieMedia.setFormat("mp4");

                movieMediaRepository.save(movieMedia);

                log.info("Processed and saved movie media for resolution: " + resolutions[i]);

                tempInputFile.delete();
                outputFile.delete();

            } catch (Exception e) {
                log.error("Failed to convert and save movie media for file: " + fileName, e);
            }
        }
    }
}

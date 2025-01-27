package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.FileUploadTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.model.MovieMedia;
import com.example.cinema.cinemaws.repository.MovieMediaRepository;
import com.example.cinema.cinemaws.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.*;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "movieMedia")
public class MovieMediaService {
    private final MovieMediaRepository movieMediaRepository;
    private final MovieRepository movieRepository;
    private final FileService fileService;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    @Value("${sqs.queue.url}")
    private String sqsQueueUrl;

    public void createMovieMedia(Long movieId, MultipartFile file) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
        MovieMedia movieMedia = new MovieMedia();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "_" + movie.getTitle() + "." + extension;
        String key = "movie/" + movie.getMovieId() + "/original/";
        fileService.uploadFile(FileUploadTO.builder()
                .fileName(fileName)
                .filePath(key)
                .file(file)
                .build());
        movieMedia.setMovie(movie);
        movieMedia.setFileName(movie.getTitle() + "." + extension);
        movieMedia.setFileSize(file.getSize());
        movieMedia.setQuality("4K");
        movieMedia.setFormat(extension);

        Map<String, Object> movieConversionRequest = new HashMap<>();
        movieConversionRequest.put("fileName", fileName);
        movieConversionRequest.put("filePath", key);
        movieConversionRequest.put("movieId", movie.getMovieId());
        try {
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(sqsQueueUrl)
                    .messageBody(objectMapper.writeValueAsString(movieConversionRequest))
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Cacheable
    public List<MovieMedia> getAllMovieMedia() {
        return movieMediaRepository.findAll();
    }

    @Cacheable(key = "{#page, #size, #sortBy, #sortOrder, #quality}")
    public Page<MovieMedia> getMovieMedia(int page, int size, String sortBy, String sortOrder, String quality) {
        int adjustedPage = Math.max(page - 1, 0);
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy);
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);
        Specification<MovieMedia> specification = createMovieMediaSpecification(quality);
        return movieMediaRepository.findAll(specification, pageable);
    }

    private Specification<MovieMedia> createMovieMediaSpecification(String quality) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (quality != null && !quality.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("quality"), quality));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Cacheable(key = "#id")
    public MovieMedia getMovieMediaById(Long id) {
        return movieMediaRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
    }

    @CachePut(key = "#id")
    public MovieMedia updateMovieMedia(Long id, MovieMedia movieMediaDetails) {
        MovieMedia existingMedia = movieMediaRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        existingMedia.setQuality(movieMediaDetails.getQuality());
        existingMedia.setFileSize(movieMediaDetails.getFileSize());
        existingMedia.setFileName(movieMediaDetails.getFileName());
        existingMedia.setFormat(movieMediaDetails.getFormat());

        return movieMediaRepository.save(existingMedia);
    }

    @CacheEvict(key = "#id")
    public void deleteMovieMedia(Long id) {
        MovieMedia movieMedia = movieMediaRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
        movieMediaRepository.delete(movieMedia);
    }
}

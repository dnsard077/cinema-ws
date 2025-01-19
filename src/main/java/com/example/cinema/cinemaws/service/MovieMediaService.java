package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.FileUploadTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.model.MovieMedia;
import com.example.cinema.cinemaws.repository.MovieMediaRepository;
import com.example.cinema.cinemaws.repository.MovieRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "movieMedia")
public class MovieMediaService {
    private final MovieMediaRepository movieMediaRepository;
    private final MovieRepository movieRepository;
    private final FileService fileService;

    @CachePut(key = "#movieId")
    public MovieMedia createMovieMedia(Long movieId, MultipartFile file) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
        MovieMedia movieMedia = new MovieMedia();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        fileService.uploadFile(FileUploadTO.builder()
                .fileName(movie.getTitle() + UUID.randomUUID())
                .filePath("movie/")
                .file(file)
                .build());
        movieMedia.setMovie(movie);
        movieMedia.setFileName(movie.getTitle() + "." + extension);
        movieMedia.setFileSize(file.getSize());
        movieMedia.setQuality("4K");
        movieMedia.setFormat(extension);

        return movieMediaRepository.save(movieMedia);
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

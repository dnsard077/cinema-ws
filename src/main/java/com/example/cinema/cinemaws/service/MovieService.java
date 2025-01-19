package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Movie;
import com.example.cinema.cinemaws.repository.MovieRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "movie")
public class MovieService {
    private final MovieRepository movieRepository;

    @CachePut(key = "#movie.id")
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Cacheable
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Cacheable(key = "{#page, #size, #sortBy, #sortOrder, #title, #genre}")
    public Page<Movie> getAllMovies(int page, int size, String sortBy, String sortOrder, String title, String genre) {
        int adjustedSize = page > 0 ? page - 1 : 0;
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy);
        Pageable pageable = PageRequest.of(adjustedSize, size, sort);
        Specification<Movie> movieSpecification = createMovieSpesification(title, genre);
        return movieRepository.findAll(movieSpecification, pageable);
    }

    private Specification<Movie> createMovieSpesification(String title, String genre) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            if (genre != null && !genre.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("genre"), genre));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Cacheable(key = "#id")
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
    }

    @CachePut(key = "#id")
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
        existingMovie.setTitle(movieDetails.getTitle());
        existingMovie.setGenre(movieDetails.getGenre());
        existingMovie.setDuration(movieDetails.getDuration());
        existingMovie.setReleaseDate(movieDetails.getReleaseDate());
        return movieRepository.save(existingMovie);
    }

    @CacheEvict(key = "#id")
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
        movieRepository.delete(movie);
    }
}

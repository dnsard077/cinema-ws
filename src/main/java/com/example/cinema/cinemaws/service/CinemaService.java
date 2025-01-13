package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.Cinema;
import com.example.cinema.cinemaws.repository.CinemaRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
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
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public Cinema createCinema(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }

    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAll();
    }

    public Cinema getCinemaById(Long id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
    }

    public Cinema updateCinema(Long id, Cinema cinema) {
        Cinema existingCinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));

        existingCinema.setName(cinema.getName());
        existingCinema.setLocation(cinema.getLocation());
        existingCinema.setContactNumber(cinema.getContactNumber());

        return cinemaRepository.save(existingCinema);
    }

    public void deleteCinema(Long id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ResponseCodeEn.RESOURCE_NOT_FOUND));
        cinemaRepository.delete(cinema);
    }

    public Page<Cinema> getAllCinemas(int page, int size, String sortBy, String sortOrder, String name, String location) {
        int adjustedPage = page > 0 ? page - 1 : 0;
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(adjustedPage, size, sort);
        Specification<Cinema> cinemaSpecification = createSpecification(name, location);
        return cinemaRepository.findAll(cinemaSpecification, pageable);
    }

    private Specification<Cinema> createSpecification(String name, String location) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if (location != null && !location.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("location"), "%" + location + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
package com.example.cinema.cinemaws.repository;

import com.example.cinema.cinemaws.model.MovieMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieMediaRepository extends JpaRepository<MovieMedia, Long>, JpaSpecificationExecutor<MovieMedia> {
}

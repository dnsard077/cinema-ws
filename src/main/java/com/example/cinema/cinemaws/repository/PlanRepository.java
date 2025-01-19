package com.example.cinema.cinemaws.repository;

import com.example.cinema.cinemaws.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlanRepository extends JpaRepository<Plan, Long>, JpaSpecificationExecutor<Plan> {
}

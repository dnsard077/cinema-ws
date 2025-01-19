package com.example.cinema.cinemaws.repository;

import com.example.cinema.cinemaws.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> , JpaSpecificationExecutor<Subscription> {
}

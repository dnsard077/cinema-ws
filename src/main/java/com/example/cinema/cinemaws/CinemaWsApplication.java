package com.example.cinema.cinemaws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
public class CinemaWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaWsApplication.class, args);
    }

}

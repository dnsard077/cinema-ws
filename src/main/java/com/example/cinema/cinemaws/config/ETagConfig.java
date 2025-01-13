package com.example.cinema.cinemaws.config;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class ETagConfig {
    @Bean
    public Filter ShallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}

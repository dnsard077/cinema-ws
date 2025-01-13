package com.example.cinema.cinemaws.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void set(String key, String value) {
        log.info("Setting value in Redis: key={}, value={}", key, value);
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, Duration time) {
        log.info("Setting value in Redis with expiration: key={}, value={}, duration={}", key, value, time);
        redisTemplate.opsForValue().set(key, value, time);
    }

    public String get(String key) {
        log.info("Getting value from Redis: key={}", key);
        String value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            log.info("Value retrieved from Redis: key={}, value={}", key, value);
        } else {
            log.warn("No value found in Redis for key={}", key);
        }
        return value;
    }

    public void remove(String key) {
        log.info("Removing key from Redis: key={}", key);
        redisTemplate.delete(key);
    }

    public Boolean isExists(String key) {
        log.info("Checking existence of key in Redis: key={}", key);
        Boolean exists = redisTemplate.hasKey(key);
        log.info("Key existence check: key={}, exists={}", key, exists);
        return exists;
    }
}
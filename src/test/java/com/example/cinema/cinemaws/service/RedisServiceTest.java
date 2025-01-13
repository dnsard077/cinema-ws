package com.example.cinema.cinemaws.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void givenKeyAndValueWhenSetThenValueIsStored() {
        // Given
        String key = "testKey";
        String value = "testValue";

        // When
        redisService.set(key, value);

        // Then
        verify(valueOperations).set(key, value);
    }

    @Test
    void givenKeyValueAndDurationWhenSetThenValueIsStoredWithExpiration() {
        // Given
        String key = "testKey";
        String value = "testValue";
        Duration duration = Duration.ofMinutes(5);

        // When
        redisService.set(key, value, duration);

        // Then
        verify(valueOperations).set(key, value, duration);
    }

    @Test
    void givenKeyWhenGetThenReturnValue() {
        // Given
        String key = "testKey";
        String expectedValue = "testValue";
        when(valueOperations.get(key)).thenReturn(expectedValue);

        // When
        String result = redisService.get(key);

        // Then
        assertEquals(expectedValue, result);
        verify(valueOperations).get(key);
    }

    @Test
    void givenKeyWhenGetThenReturnNull() {
        // Given
        String key = "nonExistentKey";
        when(valueOperations.get(key)).thenReturn(null);

        // When
        String result = redisService.get(key);

        // Then
        assertNull(result);
        verify(valueOperations).get(key);
    }

    @Test
    void givenKeyWhenRemoveThenKeyIsDeleted() {
        // Given
        String key = "testKey";

        // When
        redisService.remove(key);

        // Then
        verify(redisTemplate).delete(key);
    }

    @Test
    void givenKeyWhenIsExistsThenReturnTrue() {
        // Given
        String key = "testKey";
        when(redisTemplate.hasKey(key)).thenReturn(true);

        // When
        Boolean exists = redisService.isExists(key);

        // Then
        assertTrue(exists);
        verify(redisTemplate).hasKey(key);
    }

    @Test
    void givenKeyWhenIsExistsThenReturnFalse() {
        // Given
        String key = "nonExistentKey";
        when(redisTemplate.hasKey(key)).thenReturn(false);

        // When
        Boolean exists = redisService.isExists(key);

        // Then
        assertFalse(exists);
        verify(redisTemplate).hasKey(key);
    }
}
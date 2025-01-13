package com.example.cinema.cinemaws.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final String secretKeyString = "V29yQ29tUGFzc3dvcmQyMDAV29yQ29tUGFzc3dvcmQyMDAV29yQ29tUGFzc3dvcmQyMDAV29yQ29tUGFzc3dvcmQyMDA";
    private final long accessTokenExpirationTime = 3600000;
    private final long refreshTokenExpirationTime = 604800000;
    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtil, "secretKeyString", secretKeyString);
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpirationTime", accessTokenExpirationTime);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenExpirationTime", refreshTokenExpirationTime);
        jwtUtil.init();
    }

    @Test
    void givenValidData_whenGenerateAccessToken_thenTokenIsCreated() {
        // Given
        String username = "testUser";
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");

        // When
        String token = jwtUtil.generateAccessToken(username, claims);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(username, jwtUtil.extractUsername(token));
    }

    @Test
    void givenValidData_whenGenerateRefreshToken_thenTokenIsCreated() {
        // Given
        String username = "testUser";
        Map<String, Object> claims = new HashMap<>();

        // When
        String token = jwtUtil.generateRefreshToken(username, claims);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(username, jwtUtil.extractUsername(token));
        Claims extractedClaims = jwtUtil.extractAllClaims(token);
        assertEquals("refresh", extractedClaims.get("type"));
    }

    @Test
    void givenValidToken_whenValidateToken_thenTokenIsValid() {
        // Given
        String username = "testUser";
        Map<String, Object> claims = new HashMap<>();
        String token = jwtUtil.generateAccessToken(username, claims);

        // When
        boolean isValid = jwtUtil.validateToken(token, username);

        // Then
        assertTrue(isValid);
    }

    @Test
    void givenShortExpiryToken_whenValidateAfterWait_thenTokenIsInvalid() throws InterruptedException {
        // Given
        String username = "testUser";
        Map<String, Object> claims = new HashMap<>();
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpirationTime", 100);
        String token = jwtUtil.generateAccessToken(username, claims);
        Thread.sleep(200);

        // When
        boolean isValid = jwtUtil.validateToken(token, username);

        // Then
        assertFalse(isValid);
    }

    @Test
    void givenValidToken_whenExtractUsername_thenCorrectUsernameIsExtracted() {
        // Given
        String username = "testUser";
        Map<String, Object> claims = new HashMap<>();
        String token = jwtUtil.generateAccessToken(username, claims);

        // When
        String extractedUsername = jwtUtil.extractUsername(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    void givenValidToken_whenCheckExpiration_thenCorrectExpirationState() {
        // Given
        String username = "testUser";
        Map<String, Object> claims = new HashMap<>();
        String token = jwtUtil.generateAccessToken(username, claims);

        // When
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Then
        assertFalse(isExpired);
    }

    @Test
    void givenExpiredToken_whenCheckExpiration_thenTokenIsExpired() {
        // Given
        String username = "testUser";
        Map<String, Object> claims = new HashMap<>();
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpirationTime", -1000);
        String token = jwtUtil.generateAccessToken(username, claims);

        // When
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Then
        assertTrue(isExpired);
    }
}

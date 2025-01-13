package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.exception.ResponseException;
import com.example.cinema.cinemaws.model.User;
import com.example.cinema.cinemaws.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenUserId_whenGetUserById_thenReturnUser () {
        // Given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("username");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserById(1L);

        // Then
        assertEquals(user, result);
    }

    @Test
    void givenInvalidUserId_whenGetUserById_thenThrowException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> userService.getUserById(1L));
    }

    @Test
    void whenGetAllUsers_thenReturnUserList() {
        // Given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("username");
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertEquals(users, result);
    }

    @Test
    void givenUserDetails_whenCreateUser_thenReturnUser () {
        // Given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@example.com");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userService.createUser (user);

        // Then
        assertEquals(user, result);
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void givenUserIdAndDetails_whenUpdateUser_thenReturnUpdatedUser () {
        // Given
        User existingUser  = new User();
        existingUser .setUserId(1L);
        existingUser .setUsername("oldUsername");
        existingUser .setPassword("oldPassword");
        existingUser .setEmail("oldEmail@example.com");

        User userDetails = new User();
        userDetails.setUsername("newUsername");
        userDetails.setPassword("newPassword");
        userDetails.setEmail("newEmail@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser ));
        when(userRepository.save(any(User.class))).thenReturn(existingUser );

        // When
        User result = userService.updateUser (1L, userDetails);

        // Then
        assertEquals("newUsername", result.getUsername());
        assertEquals("newPassword", result.getPassword());
        assertEquals("newEmail@example.com", result.getEmail());
    }

    @Test
    void givenInvalidUserId_whenUpdateUser_thenThrowException() {
        // Given
        User userDetails = new User();
        userDetails.setUsername("newUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> userService.updateUser (1L, userDetails));
    }

    @Test
    void givenUserId_whenDeleteUser_thenUserIsDeleted() {
        // Given
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        userService.deleteUser (1L);

        // Then
        verify(userRepository).delete(user);
    }

    @Test
    void givenInvalidUserId_whenDeleteUser_thenThrowException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseException.class, () -> userService.deleteUser (1L));
    }

    @Test
    void whenGetAllUsersWithPagination_thenReturnPagedUsers() {
        // Given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("username");
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl(users));

        // When
        Page<User> result = userService.getAllUsers(1, 10, "username", "asc", null, null);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(users, result.getContent());
    }

    @Test
    void givenUserWithValidationError_whenCreateUser_thenThrowException() {
        // Given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("invalidEmail");

        doThrow(new ConstraintViolationException(new HashSet<>()))
                .when(validationService).validate(user);

        // When & Then
        assertThrows(ConstraintViolationException.class, () -> userService.createUser (user));
    }
}
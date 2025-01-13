package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.User;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private ApiResponseFactory apiResponseFactory;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1L);
        user.setUsername("John Doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    public void givenUserId_whenGetUserById_thenReturnUser() {
        // Given
        when(userService.getUserById(1L)).thenReturn(user);
        ApiResponseTO<User> expectedApiResponse = ApiResponseTO.<User>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(user)
                .build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.SUCCESS_OPERATION), any(User.class)))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<User>> response = userController.getUserById(1L);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenPaginationParams_whenGetAllUsers_thenReturnUserPage() {
        // Given
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userService.getAllUsers(1, 10, "userId", "asc", null, null)).thenReturn(userPage);
        ApiResponseTO<Page> expectedApiResponse = ApiResponseTO.<Page>builder()
                .code(ResponseCodeEn.SUCCESS_OPERATION.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_OPERATION.getCode())
                .message("success.default")
                .data(userPage)
                .build();
        when(apiResponseFactory.createResponse(eq(ResponseCodeEn.SUCCESS_OPERATION), any(Page.class)))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Page<User>>> response = userController.getAllUsers(1, 10, "userId", "asc", null, null);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenUser_whenCreateUser_thenReturnCreatedUser() {
        // Given
        when(userService.createUser(any(User.class))).thenReturn(user);
        ApiResponseTO<User> expectedApiResponse = ApiResponseTO.<User>builder()
                .code(ResponseCodeEn.SUCCESS_CREATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_CREATED.getCode())
                .message("success.default")
                .data(user)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, user))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<User>> response = userController.createUser(user);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenUserIdAndDetails_whenUpdateUser_thenReturnUpdatedUser() {
        // Given
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);
        ApiResponseTO<User> expectedApiResponse = ApiResponseTO.<User>builder()
                .code(ResponseCodeEn.SUCCESS_UPDATED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_UPDATED.getCode())
                .message("success.default")
                .data(user)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, user))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<User>> response = userController.updateUser(1L, user);

        // Then
        assertEquals(ResponseEntity.ok(expectedApiResponse), response);
    }

    @Test
    public void givenUserId_whenDeleteUser_thenReturnNoContent() {
        // Given
        doNothing().when(userService).deleteUser(1L);
        ApiResponseTO<Object> expectedApiResponse = ApiResponseTO.<Object>builder()
                .code(ResponseCodeEn.SUCCESS_DELETED.getHttpStatus().value())
                .responseCode(ResponseCodeEn.SUCCESS_DELETED.getCode())
                .message("success.default")
                .data(null)
                .build();
        when(apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // When
        ResponseEntity<ApiResponseTO<Object>> response = userController.deleteUser(1L);

        // Then
        assertEquals(ResponseEntity.ok().body(expectedApiResponse), response);
        verify(userService, times(1)).deleteUser(1L);
    }
}
package com.example.cinema.cinemaws.controller;

import com.example.cinema.cinemaws.dto.ApiResponseTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.model.User;
import com.example.cinema.cinemaws.service.ApiResponseFactory;
import com.example.cinema.cinemaws.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTO<User>> getUserById(@PathVariable Long id) {
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponseTO<Page<User>>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "userId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_OPERATION, userService.getAllUsers(page, size, sortBy, sortOrder, username, email));
    }

    @PostMapping
    public ResponseEntity<ApiResponseTO<User>> createUser(@RequestBody User user) {
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_CREATED, userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseTO<User>> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_UPDATED, userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseTO<Object>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return apiResponseFactory.createResponse(ResponseCodeEn.SUCCESS_DELETED);
    }
}
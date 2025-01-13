package com.example.cinema.cinemaws.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCodeEn {
    // Default Response Codes
    SUCCESS_OPERATION("SUCCESS_OPERATION", HttpStatus.OK, "success.default"),
    SUCCESS_CREATED("SUCCESS_CREATED", HttpStatus.OK, "success.created"),
    SUCCESS_UPDATED("SUCCESS_UPDATED", HttpStatus.OK, "success.updated"),
    SUCCESS_DELETED("SUCCESS_DELETED", HttpStatus.OK, "success.deleted"),
    OPERATION_FAILED("OPERATION_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, "error.operation_failed"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED, "error.invalid_credentials"),
    ACCESS_DENIED("ACCESS_DENIED", HttpStatus.FORBIDDEN, "error.access_denied"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "error.internal_server"),
    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST, "error.invalid_input"),
    CONSTRAINT_VIOLATION("CONSTRAINT_VIOLATION", HttpStatus.BAD_REQUEST, "error.constraint_violation"),
    RESOURCE_LOCKED("RESOURCE_LOCKED", HttpStatus.LOCKED, "error.resource_locked"),
    INVALID_TOKEN("INVALID_TOKEN", HttpStatus.UNAUTHORIZED, "error.invalid.token"),
    INVALID_ID("INVALID_ID", HttpStatus.BAD_REQUEST, "error.invalid.id"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND, "error.not_found"),

    // User Response Codes
    USER_LOGIN("USER_LOGIN", HttpStatus.OK, "success.login"),
    USER_CREATED("USER_CREATED", HttpStatus.CREATED, "success.user.created"),
    USER_NOT_FOUND("USER_NOT_FOUND", HttpStatus.NOT_FOUND, "error.user.not_found"),
    USER_WRONG_PASSWORD("USER_WRONG_PASSWORD", HttpStatus.UNAUTHORIZED, "error.user.wrong_password"),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", HttpStatus.CONFLICT, "error.user.already_exists"),

    // OTP Response Codes
    OTP_SEND("OTP_SEND", HttpStatus.OK, "error.otp.send"),
    OTP_FAILED("OTP_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, "error.otp.send"),
    OTP_LIMIT("OTP_LIMIT", HttpStatus.TOO_MANY_REQUESTS, "error.otp.limit"),
    OTP_COOLDOWN("OTP_COOLDOWN", HttpStatus.TOO_MANY_REQUESTS, "error.otp.cooldown"),
    OTP_SENT("OTP_SENT", HttpStatus.OK, "error.otp.sent");

    private final HttpStatus httpStatus;
    private final String code;
    private final String messageKey;

    ResponseCodeEn(String code, HttpStatus httpStatus, String messageKey) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.messageKey = messageKey;
    }
}
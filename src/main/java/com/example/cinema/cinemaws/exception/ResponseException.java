package com.example.cinema.cinemaws.exception;

import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import lombok.Getter;

@Getter
public class ResponseException extends RuntimeException {
    private ResponseCodeEn responseCodeEn;

    public ResponseException() {
        super();
    }

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseException(Throwable cause) {
        super(cause);
    }

    public ResponseException(ResponseCodeEn responseCodeEn) {
        super();
        this.responseCodeEn = responseCodeEn;
    }

}

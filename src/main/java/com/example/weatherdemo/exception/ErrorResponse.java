package com.example.weatherdemo.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private ZonedDateTime timestamp;

    private int status;

    private String message;

    private String errors;

    private String exception;

    private String path;

    public static ErrorResponse create(HttpStatus httpStatus, Exception ex,
            String message, StringBuffer requestUrl) {
        String path = null;
        if (requestUrl != null) {
            path = requestUrl.toString();
        }
        return create(httpStatus, ex, message, path);
    }

    public static ErrorResponse create(HttpStatus httpStatus, Exception ex,
            String message, String path) {
        ErrorResponse apiError = new ErrorResponse();
        apiError.setTimestamp(ZonedDateTime.now());
        apiError.setStatus(httpStatus.value());
        apiError.setMessage(message);
        apiError.setErrors(httpStatus.getReasonPhrase());
        apiError.setException(ex.getClass().getName());
        apiError.setPath(path);
        return apiError;
    }
}
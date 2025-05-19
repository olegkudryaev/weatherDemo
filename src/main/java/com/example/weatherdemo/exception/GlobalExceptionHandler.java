package com.example.weatherdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(HttpServletRequest req, Exception ex) {
        log.error("Invalid argument: ", ex);
        return ErrorResponse.create(HttpStatus.BAD_REQUEST, ex, ex.getMessage(), req.getRequestURL());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(HttpServletRequest req, Exception ex) {
        log.error("Entity not found: ", ex);
        return ErrorResponse.create(HttpStatus.NOT_FOUND, ex, ex.getMessage(), req.getRequestURL());
    }

    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleResourceAccessException(HttpServletRequest req, Exception ex) {
        log.error("Resource access error: ", ex);
        return ErrorResponse.create(HttpStatus.SERVICE_UNAVAILABLE, ex, ex.getMessage(), req.getRequestURL());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(HttpServletRequest req, Exception ex) {
        log.error("Unexpected error: ", ex);
        return ErrorResponse.create(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getMessage(), req.getRequestURL());
    }
} 
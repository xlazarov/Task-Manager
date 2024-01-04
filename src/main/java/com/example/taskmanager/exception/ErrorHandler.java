package com.example.taskmanager.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global error handler for the application.
 * Handles various exceptions and translates them into appropriate HTTP responses.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    /**
     * Handles ConstraintViolationException and returns a BAD_REQUEST response.
     *
     * @param e The ConstraintViolationException to handle.
     * @return A ResponseEntity with a BAD_REQUEST status and a message indicating validation failure.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Validation failed with ConstraintViolationException: {}", e.getMessage());
        Map<String, String> errorMap = new HashMap<>();
        e.getConstraintViolations().forEach(violation ->
                errorMap.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a BAD_REQUEST response.
     *
     * @param e The MethodArgumentNotValidException to handle.
     * @return A ResponseEntity with a BAD_REQUEST status and a message indicating validation failure.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation failed with MethodArgumentNotValidException: {}", e.getMessage());
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errorMap.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    /**
     * Handles generic exceptions and returns an INTERNAL_SERVER_ERROR response.
     *
     * @param e The Exception to handle.
     * @return A ResponseEntity with an INTERNAL_SERVER_ERROR status and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("An unexpected error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
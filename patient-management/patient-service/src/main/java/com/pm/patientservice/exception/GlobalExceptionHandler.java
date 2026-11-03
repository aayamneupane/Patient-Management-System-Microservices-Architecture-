package com.pm.patientservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// lets you handle cross-cutting concerns—like global exceptions, model attributes, and data binding
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // annotation used in Spring Boot to intercept
    // and handle validation errors that occur when
    // a request body fails the constraints defined by Jakarta/Hibernate Validation
    // annotations (like @NotNull, @Size, or @Email
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        // Collects each field validation error into the map as fieldName → error message.
        ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        // Log a warning (not an error): a duplicate email is an expected client-side
        // validation failure, not a server fault, so WARN keeps it visible without
        // triggering error alerts.
        // {} is an SLF4J placeholder: ex.getMessage() is only formatted into the
        // string if WARN logging is enabled, which avoids needless string concatenation.
        log.warn("Email address already exists {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Email address already exists!");
        return ResponseEntity.badRequest().body(errors);
    }
}

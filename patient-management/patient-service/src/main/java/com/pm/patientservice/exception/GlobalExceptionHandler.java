package com.pm.patientservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// lets you handle cross-cutting concerns—like global exceptions, model attributes, and data binding
@ControllerAdvice
public class GlobalExceptionHandler {
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
}

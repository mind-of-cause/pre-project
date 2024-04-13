package ru.kata.spring.boot_security.demo.configs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", ex.getStatus().value());
        errorDetails.put("error", ex.getStatus().getReasonPhrase());
        errorDetails.put("message", ex.getReason());

        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }
}
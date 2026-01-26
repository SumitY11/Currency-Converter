package com.currency.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 500);
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, Object>> handleValidationErrors(
        MethodArgumentNotValidException ex) {

    Map<String, Object> error = new HashMap<>();
    error.put("timestamp", LocalDateTime.now());
    error.put("status", 400);
    error.put("error", "Bad Request");

    String message = ex.getBindingResult()
            .getFieldErrors()
            .get(0)
            .getDefaultMessage();

    error.put("message", message);

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(CurrencyNotFoundException.class)
public ResponseEntity<Map<String, Object>> handleCurrencyNotFound(
        CurrencyNotFoundException ex) {

    Map<String, Object> error = new HashMap<>();
    error.put("timestamp", LocalDateTime.now());
    error.put("status", 404);
    error.put("error", "Not Found");
    error.put("message", ex.getMessage());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}


}

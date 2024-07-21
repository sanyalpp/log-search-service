package com.cribl.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is the global exception handler.
 */
@Slf4j
@ControllerAdvice
public class LogSearchApplicationExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleValidationExceptions(IllegalArgumentException ex, WebRequest request) {
        // Log the exception details for debugging purposes
        log.error("Error occurred", ex);
        Map<String, String> validationErrors = new HashMap<>();
        String message = ex.getMessage();
        validationErrors.put("message", message);
        validationErrors.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }
}

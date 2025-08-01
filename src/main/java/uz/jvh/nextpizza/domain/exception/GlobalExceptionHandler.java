package uz.jvh.nextpizza.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        // Return error details without translation
        errorResponse.put("errorCode", ex.getErrorCode());
        errorResponse.put("errorMessage", ex.getErrorMessage());
        errorResponse.put("timestamp", LocalDateTime.now());

        // Get HttpStatus and return response
        HttpStatus status = ex.getHttpStatus();
        return new ResponseEntity<>(errorResponse, status);
    }

    // General Exception handler for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", "500");
        errorResponse.put("errorMessage", "Internal server error: " + ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package uz.jvh.nextpizza.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import uz.jvh.nextpizza.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * NextPizzaException - barcha custom exceptionlar uchun
     * (Pizza, User, Drink, Cart, Order va boshqalar)
     */
    @ExceptionHandler(NextPizzaException.class)
    public ResponseEntity<ErrorResponse> handleNextPizzaException(NextPizzaException ex, HttpServletRequest request) {

        log.error("NextPizzaException: {} - {}", ex.getErrorCode(), ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(ex.getErrorCode().getStatus().value()).error(ex.getErrorCode().name()).
                message(ex.getErrorCode().getMessage()).path(request.getRequestURI()).details(ex.getDetails()).build();

        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    /**
     * Spring Security - BadCredentialsException
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value()).error("UNAUTHORIZED").message("Email yoki parol noto'g'ri")
                .path(request.getRequestURI()).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Spring Security - UsernameNotFoundException
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value()).error("USER_NOT_FOUND").message("Foydalanuvchi topilmadi")
                .path(request.getRequestURI()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * JWT token exceptionlari
     */
    @ExceptionHandler({ExpiredJwtException.class, MalformedJwtException.class, SignatureException.class})
    public ResponseEntity<ErrorResponse> handleJwtException(RuntimeException ex, HttpServletRequest request) {

        String message = "Token noto'g'ri yoki muddati tugagan";
        String errorCode = "INVALID_TOKEN";

        if (ex instanceof ExpiredJwtException) {
            message = "Token muddati tugagan";
            errorCode = "TOKEN_EXPIRED";
        } else if (ex instanceof MalformedJwtException) {
            message = "Token formati noto'g'ri";
            errorCode = "MALFORMED_TOKEN";
        } else if (ex instanceof SignatureException) {
            message = "Token imzosi noto'g'ri";
            errorCode = "INVALID_SIGNATURE";
        }

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value()).error(errorCode).message(message).
                path(request.getRequestURI()).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Validation errorlari (@Valid annotation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ErrorResponse.ValidationError> validationErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.add(ErrorResponse.ValidationError.builder().field(fieldName).message(errorMessage).build());
        });

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now()).
                status(HttpStatus.BAD_REQUEST.value()).error("VALIDATION_ERROR").message("Validation xatosi").
                path(request.getRequestURI()).validationErrors(validationErrors).build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Barcha boshqa exceptionlar uchun umumiy handler
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

        log.error("Unexpected error: ", ex);

        ErrorResponse errorResponse = ErrorResponse.builder().
                timestamp(LocalDateTime.now()).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                error("INTERNAL_SERVER_ERROR").message("Serverda xatolik yuz berdi").
                path(request.getRequestURI()).build();

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}

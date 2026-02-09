package uz.jvh.nextpizza.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import uz.jvh.nextpizza.dto.response.error.ErrorResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Filter'dan exception olinadi
        Exception exception = (Exception) request.getAttribute("exception");

        String message = "Autentifikatsiya talab qilinadi";

        if (exception != null) {
            if (exception instanceof io.jsonwebtoken.ExpiredJwtException) {
                message = "Token muddati tugagan";
            } else if (exception instanceof io.jsonwebtoken.MalformedJwtException) {
                message = "Token formati noto'g'ri";
            } else if (exception instanceof io.jsonwebtoken.security.SignatureException) {
                message = "Token imzosi noto'g'ri";
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message(message)
                .path(request.getRequestURI())
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); // LocalDateTime uchun
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}

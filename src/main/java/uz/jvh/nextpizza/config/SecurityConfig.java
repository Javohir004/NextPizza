package uz.jvh.nextpizza.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import uz.jvh.nextpizza.security.CustomUserDetailsService;
import uz.jvh.nextpizza.security.JwtTokenFilter;
import uz.jvh.nextpizza.security.JwtTokenUtil;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper; // JSON mapper for custom error responses
    private final JwtTokenUtil jwtTokenUtil; // Utility for handling JWT tokens
    private final CustomUserDetailsService userDetailsService; // User details service implementation

    /**
     * Custom AuthenticationEntryPoint to handle unauthorized access (401 error).
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            String errorPath = request.getRequestURI();
            String errorMessage = authException.getMessage();
            int errorCode = 401;

            // Create error response object
            AppErrorRequest appErrorDto = new AppErrorRequest(
                    errorPath, errorMessage, errorCode, LocalDateTime.now()
            );

            // Set response details
            response.setContentType("application/json");
            response.setStatus(errorCode);
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                objectMapper.writeValue(outputStream, appErrorDto);
                outputStream.flush();
            }
        };
    }

    /**
     * Security filter chain configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable() // Disable CSRF protection (ensure this is safe for your use case)
                .cors().configurationSource(corsConfigurationSource()).and() // Enable CORS with custom configuration
                .authorizeHttpRequests()
                .requestMatchers(
                        "/",// Public endpoints
                        "/hello",
                        "/api/auth/login/**",
                        "/api/auth/register/**",
                        "/api/auth/sent-email/**",
                        "/api/auth/sent-password/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated() // Protect all other endpoints
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint()) // Use custom authentication entry point
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenUtil, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class) // Add custom JWT filter
                .build();
    }

    /**
     * Password encoder for encoding and validating passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Custom CORS configuration for handling cross-origin requests.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // Allow all origins
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed methods
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept")); // Allowed headers
        //corsConfiguration.setAllowCredentials(true); // Allow credentials (e.g., cookies, authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * AuthenticationManager bean for managing authentication logic.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()); // Use BCrypt for password encoding
        return authenticationManagerBuilder.build();
    }

    /**
     * DTO for custom error responses.
     */
    @Getter
    @AllArgsConstructor
    public static class AppErrorRequest {
        private String errorPath;
        private String errorMessage;
        private Integer errorCode;
        private LocalDateTime timeStamp;
    }
}

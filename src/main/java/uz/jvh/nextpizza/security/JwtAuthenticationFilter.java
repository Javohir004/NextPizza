package uz.jvh.nextpizza.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.jvh.nextpizza.service.JwtService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Ochiq endpoint'lar - bu endpoint'larda JWT tekshirilmaydi
    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/api/auth/register",
            "/api/auth/login",
            "/api/test/public",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Public endpoint'larni tekshirish
        String requestPath = request.getServletPath();
        if (isPublicUrl(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Agar Authorization header yo'q bo'lsa yoki Bearer bilan boshlanmasa
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            // Token muddati tugagan
            request.setAttribute("exception", ex);
        } catch (io.jsonwebtoken.MalformedJwtException ex) {
            // Token formati noto'g'ri
            request.setAttribute("exception", ex);
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            // Token imzosi noto'g'ri
            request.setAttribute("exception", ex);
        } catch (Exception ex) {
            // Boshqa xatolar
            request.setAttribute("exception", ex);
        }

        filterChain.doFilter(request, response);
    }

    // URL public ekanligini tekshirish
    private boolean isPublicUrl(String requestPath) {
        return PUBLIC_URLS.stream().anyMatch(requestPath::startsWith);
    }
}

package uz.jvh.nextpizza.service.auth;


import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.jvh.nextpizza.dto.request.auth.LoginRequest;
import uz.jvh.nextpizza.dto.request.auth.RegisterRequest;
import uz.jvh.nextpizza.dto.response.auth.AuthenticationResponse;
import uz.jvh.nextpizza.enomerator.ErrorCode;
import uz.jvh.nextpizza.entity.User;
import uz.jvh.nextpizza.exception.NextPizzaException;
import uz.jvh.nextpizza.repository.UserRepository;
import uz.jvh.nextpizza.enomerator.Role;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * Ro'yxatdan o'tish
     */
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Email mavjudligini tekshirish
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new NextPizzaException(ErrorCode.EMAIL_ALREADY_EXISTS, request.getEmail());
        }

        // Yangi user yaratish
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .balance(request.getBalance())
                .address(request.getAddress())
                .birthDate(request.getBirthDate())
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);

        // Token yaratish
        var jwtToken = jwtTokenProvider.generateToken(user);
        var refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Tizimga kirish
     */
    public AuthenticationResponse login(LoginRequest request) {
        try {
            // Authentication
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new NextPizzaException(ErrorCode.INVALID_CREDENTIALS, ex.getMessage());
        }

        // User topish
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NextPizzaException(ErrorCode.USER_NOT_FOUND, request.getEmail()));

        // Token yaratish
        String jwtToken = jwtTokenProvider.generateToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}

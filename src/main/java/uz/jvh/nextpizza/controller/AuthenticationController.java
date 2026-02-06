package uz.jvh.nextpizza.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.jvh.nextpizza.dto.request.LoginRequest;
import uz.jvh.nextpizza.dto.request.RegisterRequest;
import uz.jvh.nextpizza.dto.response.AuthenticationResponse;
import uz.jvh.nextpizza.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication va Registration endpoint'lari")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Ro'yxatdan o'tish", description = "Yangi foydalanuvchi ro'yxatdan o'tadi va JWT token oladi")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Tizimga kirish", description = "Mavjud foydalanuvchi tizimga kiradi va JWT token oladi")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}

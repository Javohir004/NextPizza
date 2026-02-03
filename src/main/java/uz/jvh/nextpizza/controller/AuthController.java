package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.jvh.nextpizza.dto.request.UserRequest;
import uz.jvh.nextpizza.dto.response.JwtResponse;
import uz.jvh.nextpizza.dto.response.LoginDto;
import uz.jvh.nextpizza.dto.response.UserResponse;
import uz.jvh.nextpizza.service.AuthService;


@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRequest userRequest) {
        return authService.save(userRequest);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

}

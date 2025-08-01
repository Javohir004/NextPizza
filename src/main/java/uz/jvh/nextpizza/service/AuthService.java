package uz.jvh.nextpizza.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uz.jvh.nextpizza.domain.dto.request.UserRequest;
import uz.jvh.nextpizza.domain.dto.response.JwtResponse;
import uz.jvh.nextpizza.domain.dto.response.LoginDto;
import uz.jvh.nextpizza.domain.dto.response.UserResponse;
import uz.jvh.nextpizza.domain.entity.User;

import uz.jvh.nextpizza.domain.exception.CustomException;
import uz.jvh.nextpizza.security.JwtTokenUtil;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;


    public UserResponse save(UserRequest user) {
        User user1 = userService.mapRequestToEntity(user);
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user1);
        return userService.mapEntityToResponse(user1);
    }

    public JwtResponse login(LoginDto loginDto) {
        User user = userService.findByUsername(loginDto.getUsername());
        if (user == null) {
            throw new CustomException("Username or password is incorrect", 40018, HttpStatus.UNAUTHORIZED);
        }
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid username or password", 4019, HttpStatus.UNAUTHORIZED);
        }
        String token = jwtTokenUtil.generateToken(user.getUsername());
        return new JwtResponse(token, user.getId(),user.getRole());
    }

}

package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.jvh.nextpizza.dto.request.UserRequest;
import uz.jvh.nextpizza.dto.response.UserResponse;
import uz.jvh.nextpizza.enomerator.Role;
import uz.jvh.nextpizza.entity.User;
import uz.jvh.nextpizza.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .balance(user.getBalance())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .role(user.getRole())
                .createdDate(user.getCreated())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public UserResponse updateUser(@PathVariable("id") Long id, @RequestBody UserRequest userRequest) {
        return userService.update(id, userRequest);
    }

    @GetMapping("/all-user")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping("/User-role/{role}/owner")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.findByRole(role));
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("Foydalanuvchi muvaffaqiyatli o'chirildi.");
    }


    @GetMapping("/find-by-id/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }


    @GetMapping("/my-balance")
    public ResponseEntity<BigDecimal> getUserBalance(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getUserBalance(userId));
    }


}

package uz.jvh.nextpizza.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.jvh.nextpizza.dto.request.UserRequest;
import uz.jvh.nextpizza.dto.response.UserResponse;
import uz.jvh.nextpizza.enomerator.UserRole;
import uz.jvh.nextpizza.entity.User;
import uz.jvh.nextpizza.service.AuthService;
import uz.jvh.nextpizza.service.UserService;
import java.util.List;
import java.util.UUID;


@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable("id") UUID id, @RequestBody UserRequest userRequest) {
        return userService.update(id, userRequest);
    }


    @GetMapping("/all-user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllJ();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/User-role/{role}/owner")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable UserRole role) {
        List<User> users = userService.findByRole(role);
        return ResponseEntity.ok(users);
    }



    @PostMapping("/create-admin")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userCreateDTO) {
        return ResponseEntity.ok(authService.save(userCreateDTO));
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("Foydalanuvchi muvaffaqiyatli o'chirildi.");
    }


    @GetMapping("/find-by-id/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable UUID userId) {
        User user = userService.findByIdJ(userId);
        return ResponseEntity.ok(user);
    }


    @GetMapping("/my-balance")
    public ResponseEntity<Double> getUserBalance(@RequestParam UUID userId) {
        Double userBalance = userService.getUserBalance(userId);
        return ResponseEntity.ok(userBalance);
    }


}

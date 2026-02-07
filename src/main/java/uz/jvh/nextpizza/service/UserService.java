package uz.jvh.nextpizza.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.jvh.nextpizza.dto.request.UserRequest;
import uz.jvh.nextpizza.dto.response.UserResponse;
import uz.jvh.nextpizza.enomerator.Role;
import uz.jvh.nextpizza.entity.User;
import uz.jvh.nextpizza.exception.UserNotFoundException;
import uz.jvh.nextpizza.repository.OrderRepository;
import uz.jvh.nextpizza.repository.UserRepository;



import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

    ///  deyarli barcha methodlar ishlayapti

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }


    public User findByUsername(String username) {
        User userEntity = userRepository.findByFirstNameAndIsActiveTrue(username)
                .orElseThrow(() -> new UserNotFoundException("Username " + username + " not found"));
        return userEntity;
    }


    public List<User> findByRole(Role role) {
        return userRepository.findByRoleAndIsActiveTrueOrderByCreatedDesc(role);
    }

    public User findById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("UserId " + id + " not found"));
    }

    public List<User> findAll() {
        List<User> allUsers = userRepository.findAllByIsActiveTrueOrderByCreatedDesc();

        return allUsers.stream()
                .filter(user -> user.getRole().equals(Role.USER))
                .collect(Collectors.toList());
    }


    @Transactional
    public User update(Long id, UserRequest userRequest) {
        User user = findById(id);

        // Password alohida (encoding kerak)
        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        // Email uniqueness tekshirish
        if (userRequest.getEmail() != null && !userRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new RuntimeException("Bu email allaqachon band: " + userRequest.getEmail());
            }
            user.setEmail(userRequest.getEmail());
        }


        // Oddiy fieldlar (Optional bilan qisqaroq)
        Optional.ofNullable(userRequest.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(userRequest.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(userRequest.getBirthDate()).ifPresent(user::setBirthDate);
        Optional.ofNullable(userRequest.getBalance()).ifPresent(user::setBalance);
        Optional.ofNullable(userRequest.getAddress()).ifPresent(user::setAddress);
        Optional.ofNullable(userRequest.getRole()).ifPresent(user::setRole);
        Optional.ofNullable(userRequest.getPhoneNumber()).ifPresent(user::setPhoneNumber);

        // Role - faqat OWNER o'zgartira oladi (Security tekshirish kerak)
        if (userRequest.getRole() != null) {
            // Bu yerda current user role tekshirish kerak
            user.setRole(userRequest.getRole());
        }
        return userRepository.save(user);
    }

    public Double getUserBalance(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("UserId " + id + " not found"));
        return user.getBalance();
    }


    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("UserId " + userId + " not found"));;

        user.setActive(false);
        userRepository.save(user);
    }

    public User mapRequestToEntity(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .email(userRequest.getEmail())
                .birthDate(userRequest.getBirthDate())
                .phoneNumber(userRequest.getPhoneNumber())
                .balance(userRequest.getBalance())
                .build();

    }


    public UserResponse mapEntityToResponse(User user) {
        return UserResponse.builder()
                .uuid(user.getId())
                .username(user.getUsername())
                .surname(user.getLastName())
                .role(user.getRole())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .createDate(user.getCreated().toLocalDate())
                .build();
    }

}

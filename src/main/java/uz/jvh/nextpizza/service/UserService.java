package uz.jvh.nextpizza.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.jvh.nextpizza.dto.request.UserRequest;
import uz.jvh.nextpizza.dto.response.UserResponse;
import uz.jvh.nextpizza.enomerator.UserRole;
import uz.jvh.nextpizza.entity.User;
import uz.jvh.nextpizza.exception.CustomException;
import uz.jvh.nextpizza.repository.OrderRepository;
import uz.jvh.nextpizza.repository.UserRepository;



import java.util.List;
import java.util.UUID;
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
        User userEntity = userRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new CustomException("Username " + username + " not found", 4002, HttpStatus.NOT_FOUND));
        return userEntity;
    }


    public List<User> findByRole(UserRole role) {
        return userRepository.findByRoleAndIsActiveTrueOrderByCreatedDesc(role);
    }

    public User findByIdJ(UUID id) {
        return userRepository.findById(id).
                orElseThrow(() -> new CustomException("User  not found", 4002, HttpStatus.NOT_FOUND));
    }

    public List<User> findAllJ() {
        List<User> allUsers = userRepository.findAllByIsActiveTrueOrderByCreatedDesc();

        return allUsers.stream()
                .filter(user -> user.getRole().equals(UserRole.USER))
                .collect(Collectors.toList());
    }


    @Transactional
    public User update(UUID id, UserRequest userRequest) {
        User user = findByIdJ(id);
        if (userRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        user.setUsername(userRequest.getUsername() != null ? userRequest.getUsername() : user.getUsername());
        user.setSurname(userRequest.getSurname() != null ? userRequest.getSurname() : user.getSurname());
        user.setEmail(userRequest.getEmail() != null ? userRequest.getEmail() : user.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber() != null ? userRequest.getPhoneNumber() : user.getPhoneNumber());
        user.setRole(userRequest.getRole() != null ? userRequest.getRole() : user.getRole());
        user.setBirthDate(userRequest.getBirthDate() != null ? userRequest.getBirthDate() : user.getBirthDate());
        user.setBalance(userRequest.getBalance() != null ? userRequest.getBalance() : user.getBalance());

        return userRepository.save(user);
    }

    public Double getUserBalance(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new CustomException("User  not found", 4002, HttpStatus.NOT_FOUND));
        return user.getBalance();
    }


    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new CustomException("Username  not found", 4002, HttpStatus.NOT_FOUND));

        user.setActive(false);
        userRepository.save(user);
        userRepository.flush();
    }

    public User mapRequestToEntity(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .surname(userRequest.getSurname())
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
                .surname(user.getSurname())
                .role(user.getRole())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .createDate(user.getCreated().toLocalDate())
                .build();
    }

}

package uz.jvh.nextpizza.service;


import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.jvh.nextpizza.dto.request.UserRequest;
import uz.jvh.nextpizza.dto.response.UserResponse;
import uz.jvh.nextpizza.enomerator.ErrorCode;
import uz.jvh.nextpizza.enomerator.Role;
import uz.jvh.nextpizza.entity.User;
import uz.jvh.nextpizza.exception.NextPizzaException;
import uz.jvh.nextpizza.repository.OrderRepository;
import uz.jvh.nextpizza.repository.UserRepository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    ///  deyarli barcha methodlar ishlayapti

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }



    public List<UserResponse> findByRole(Role role) {
        return userRepository.findByRoleAndIsActiveTrueOrderByCreatedDesc(role)
                .stream().map(this::userToResponse).toList();
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new NextPizzaException(ErrorCode.USER_NOT_FOUND, "UserId: " + id));
        return userToResponse(user);
    }

    public String findUserFullName(Long id) {
        User user = userRepository.findByIdAndIsActiveTrue(id).
                orElseThrow(() -> new NextPizzaException(ErrorCode.USER_NOT_FOUND, "UserId: " + id));
        return  user.getFirstName() + " " +user.getLastName();
    }
    public User findByIdE(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new NextPizzaException(ErrorCode.USER_NOT_FOUND, "UserId: " + id));
        return user;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAllByIsActiveTrueOrderByCreatedDesc()
                .stream().map(this::userToResponse).toList();
    }


    @Transactional
    public UserResponse update(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new NextPizzaException(ErrorCode.USER_NOT_FOUND, "UserId: " + id));

        // Password alohida (encoding kerak)
        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        // Email uniqueness tekshirish
        if (userRequest.getEmail() != null && !userRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new NextPizzaException(ErrorCode.EMAIL_ALREADY_EXISTS ,"Email: " + userRequest.getEmail());
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
        return userToResponse(userRepository.save(user));
    }

    public BigDecimal getUserBalance(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new NextPizzaException(ErrorCode.USER_NOT_FOUND ,"UserId: " + id));
        return user.getBalance();
    }


    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new NextPizzaException(ErrorCode.USER_NOT_FOUND ,"UserId: " + userId));;

        user.setActive(false);
        userRepository.save(user);
    }

    public long getUsersCount() {
        return userRepository.countByIsActiveTrueAndRole(Role.USER);
    }

    public User mapRequestToUser(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .email(userRequest.getEmail())
                .birthDate(userRequest.getBirthDate())
                .phoneNumber(userRequest.getPhoneNumber())
                .build();

    }


    public UserResponse userToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .balance(user.getBalance())
                .address(user.getAddress())
                .active(user.isActive())
                .enabled(user.isEnabled())
                .createdDate(user.getCreated())
                .build();
    }

}

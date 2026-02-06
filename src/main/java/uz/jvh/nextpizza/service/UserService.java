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

    public User findByIdJ(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("UserId " + id + " not found"));
    }

    public List<User> findAllJ() {
        List<User> allUsers = userRepository.findAllByIsActiveTrueOrderByCreatedDesc();

        return allUsers.stream()
                .filter(user -> user.getRole().equals(Role.USER))
                .collect(Collectors.toList());
    }


    @Transactional
    public User update(Long id, UserRequest userRequest) {
        User user = findByIdJ(id);
        if (userRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        user.setFirstName(userRequest.getUsername() != null ? userRequest.getUsername() : user.getUsername());
        user.setLastName(userRequest.getSurname() != null ? userRequest.getSurname() : user.getLastName());
        user.setEmail(userRequest.getEmail() != null ? userRequest.getEmail() : user.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber() != null ? userRequest.getPhoneNumber() : user.getPhoneNumber());
        user.setRole(userRequest.getRole() != null ? userRequest.getRole() : user.getRole());
        user.setBirthDate(userRequest.getBirthDate() != null ? userRequest.getBirthDate() : user.getBirthDate());
        user.setBalance(userRequest.getBalance() != null ? userRequest.getBalance() : user.getBalance());

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
        userRepository.flush();
    }

    public User mapRequestToEntity(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.getUsername())
                .lastName(userRequest.getSurname())
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

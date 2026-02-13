package uz.jvh.nextpizza.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import uz.jvh.nextpizza.enomerator.Role;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Role role;
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;
    private BigDecimal balance;
    private String address;
    private boolean enabled;
    private boolean active;
    private LocalDateTime createdDate;
}

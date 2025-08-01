package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import uz.jvh.nextpizza.domain.enomerator.UserRole;


import java.time.LocalDate;


@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity {

    private String username;

    private String surname;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String email;

    private LocalDate birthDate;

    @Pattern(regexp = "^\\+998\\s?\\d{9}$", message = "Phone number must start with +998, followed by 9 digits, and may have an optional space.")
    private String phoneNumber;


    @Builder.Default
    private Double balance = 0.0;

    private String address;


}

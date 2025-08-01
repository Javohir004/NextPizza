package uz.jvh.nextpizza.domain.dto.response;

import lombok.*;
import uz.jvh.nextpizza.domain.enomerator.UserRole;


import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private UUID uuid;
    private String username;
    private String surname;
    private String password;
    private UserRole role;
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;
    private boolean enabled;
    private String address;
    private LocalDate createDate;
    private String passportSeries;
}

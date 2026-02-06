package uz.jvh.nextpizza.dto.response;

import lombok.*;
import uz.jvh.nextpizza.enomerator.Role;


import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long uuid;
    private String username;
    private String surname;
    private String password;
    private Role role;
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;
    private boolean enabled;
    private String address;
    private LocalDate createDate;
    private String passportSeries;
}

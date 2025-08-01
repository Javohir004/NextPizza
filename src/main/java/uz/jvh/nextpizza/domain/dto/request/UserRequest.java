package uz.jvh.nextpizza.domain.dto.request;

import lombok.*;
import uz.jvh.nextpizza.domain.enomerator.UserRole;


import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {

    private String username;
    private String surname;
    private String password;
    private UserRole role;
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;
    private Double balance = 0.0;
    private String address;
    private String passportSeries;


}

package uz.jvh.nextpizza.dto.request;

import lombok.*;
import uz.jvh.nextpizza.enomerator.Role;


import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {

    private String firstName;
    private String lastName;
    private String password;
    private Role role;
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;
    private Double balance = 0.0;
    private String address;


}

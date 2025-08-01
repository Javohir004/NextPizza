package uz.jvh.nextpizza.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.jvh.nextpizza.domain.enomerator.UserRole;


import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private String token;
    private UUID userId;
    private UserRole userRole;
}

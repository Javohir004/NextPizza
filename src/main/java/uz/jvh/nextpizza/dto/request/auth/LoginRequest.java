package uz.jvh.nextpizza.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email bo'sh bo'lmasligi kerak")
    @Email(message = "Email formati noto'g'ri")
    private String email;

    @NotBlank(message = "Parol bo'sh bo'lmasligi kerak")
    private String password;

}
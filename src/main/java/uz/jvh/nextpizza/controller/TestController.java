package uz.jvh.nextpizza.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test Controller", description = "Test endpoint'lari")
public class TestController {

    @GetMapping("/public")
    @Operation(summary = "Public endpoint", description = "Hamma ko'ra oladi, token kerak emas")
    public String publicEndpoint() {
        return "Bu public endpoint - hamma ko'ra oladi";
    }

    @GetMapping("/user")
    @Operation(summary = "User endpoint", description = "Faqat autentifikatsiya qilingan foydalanuvchilar ko'ra oladi", security = @SecurityRequirement(name = "bearerAuth"))
    public String userEndpoint() {
        return "Bu user endpoint - faqat autentifikatsiya qilingan foydalanuvchilar ko'ra oladi";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Admin endpoint", description = "Faqat ADMIN roli bo'lgan foydalanuvchilar ko'ra oladi", security = @SecurityRequirement(name = "bearerAuth"))
    public String adminEndpoint() {
        return "Bu admin endpoint - faqat adminlar ko'ra oladi";
    }

}

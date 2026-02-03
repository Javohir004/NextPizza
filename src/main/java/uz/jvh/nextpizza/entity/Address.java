package uz.jvh.nextpizza.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Address extends BaseEntity {

    private String addressText; // Foydalanuvchi yozgan manzil (matn)
    private Double latitude;    // Kenglik (Google Map nuqtasi)
    private Double longitude;   // Uzunlik (Google Map nuqtasi)

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}

package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String addressText; // Foydalanuvchi yozgan manzil (matn)
    private Double latitude;    // Kenglik (Google Map nuqtasi)
    private Double longitude;   // Uzunlik (Google Map nuqtasi)
}

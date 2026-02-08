package uz.jvh.nextpizza.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import uz.jvh.nextpizza.enomerator.DrinkType;

import java.math.BigDecimal;

@Entity(name = "drinks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Drink extends BaseEntity{
    @Column(name = "drink_name", nullable = false)
    private String drinkName;

    @Enumerated(EnumType.STRING)  // ← Muhim!
    @Column(name = "drink_type", nullable = false)
    private DrinkType drinkType;

    @Column(nullable = false)
    private Double volume;  // ← double emas, Double

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;  //
}

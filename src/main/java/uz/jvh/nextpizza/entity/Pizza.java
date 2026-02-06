package uz.jvh.nextpizza.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import uz.jvh.nextpizza.enomerator.PizzaType;

import java.math.BigDecimal;

@Entity(name = "pizzas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Pizza extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "pizza_type", nullable = false)
    private PizzaType pizzaType;

    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

}

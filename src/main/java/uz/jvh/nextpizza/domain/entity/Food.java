package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;
import uz.jvh.nextpizza.domain.enomerator.FoodType;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Food extends BaseEntity {

    private String name;

    private String description;

    private FoodType foodType;

    private double price;
}

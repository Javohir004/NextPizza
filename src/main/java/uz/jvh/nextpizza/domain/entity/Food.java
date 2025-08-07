package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;
import uz.jvh.nextpizza.domain.enomerator.FoodType;

@Entity(name = "foods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Food extends BaseEntity {

    private String name;

    private String description;

    private FoodType foodType;

    private Double price;
}

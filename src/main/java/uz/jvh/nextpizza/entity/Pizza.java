package uz.jvh.nextpizza.entity;

import jakarta.persistence.Entity;
import lombok.*;
import uz.jvh.nextpizza.enomerator.FoodType;

@Entity(name = "foods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Pizza extends BaseEntity {

    private String name;

    private String description;

    private FoodType foodType;

    private Double price;
}

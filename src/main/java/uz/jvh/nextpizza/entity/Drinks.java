package uz.jvh.nextpizza.entity;

import jakarta.persistence.Entity;
import lombok.*;
import uz.jvh.nextpizza.enomerator.DrinkType;

@Entity(name = "drinks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Drinks extends BaseEntity{
    private String drinkName;
    private DrinkType drinkType;
    private double volume;
    private double price;
}

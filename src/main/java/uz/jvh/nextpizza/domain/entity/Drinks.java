package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;
import uz.jvh.nextpizza.domain.enomerator.DrinkType;

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

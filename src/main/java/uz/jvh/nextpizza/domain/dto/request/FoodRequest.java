package uz.jvh.nextpizza.domain.dto.request;


import lombok.*;
import uz.jvh.nextpizza.domain.enomerator.FoodType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FoodRequest {

    private String name;

    private String description;

    private FoodType foodType;

    private double price;
}

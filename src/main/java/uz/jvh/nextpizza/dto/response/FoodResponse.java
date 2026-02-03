package uz.jvh.nextpizza.dto.response;

import lombok.*;
import uz.jvh.nextpizza.enomerator.FoodType;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FoodResponse {

    private UUID foodId;

    private String name;

    private String description;

    private FoodType foodType;

    private double price;

    private LocalDateTime createDate;
}

package uz.jvh.nextpizza.domain.dto.response;

import lombok.*;
import uz.jvh.nextpizza.domain.enomerator.FoodType;

import java.time.LocalDate;
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

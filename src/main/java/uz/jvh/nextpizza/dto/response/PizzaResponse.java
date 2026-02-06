package uz.jvh.nextpizza.dto.response;

import lombok.*;
import uz.jvh.nextpizza.enomerator.PizzaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PizzaResponse {

    private Long foodId;

    private String name;

    private String description;

    private PizzaType pizzaType;

    private BigDecimal price;

    private String imageUrl;

    private LocalDateTime createDate;
}

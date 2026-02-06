package uz.jvh.nextpizza.dto.request;


import lombok.*;
import uz.jvh.nextpizza.enomerator.PizzaType;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PizzaRequest {

    private String name;

    private String description;

    private PizzaType pizzaType;

    private BigDecimal price;

    private String imageUrl;
}

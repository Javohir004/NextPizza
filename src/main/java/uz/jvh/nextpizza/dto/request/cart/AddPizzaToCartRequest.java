package uz.jvh.nextpizza.dto.request.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddPizzaToCartRequest {
    @NotNull
    private Long pizzaId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
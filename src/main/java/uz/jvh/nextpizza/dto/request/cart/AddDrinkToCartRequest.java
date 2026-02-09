package uz.jvh.nextpizza.dto.request.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddDrinkToCartRequest {
    @NotNull
    public Long drinkId;

    @NotNull
    @Min(1)
    public Integer quantity;
}

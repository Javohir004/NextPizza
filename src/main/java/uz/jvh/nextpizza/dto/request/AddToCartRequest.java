package uz.jvh.nextpizza.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequest {

    private Long pizzaId;  // Pizza qo'shish uchun
    private Long drinkId;  // Drink qo'shish uchun

    @NotNull
    @Min(1)
    private Integer quantity;
}

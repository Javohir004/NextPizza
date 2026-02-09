package uz.jvh.nextpizza.dto.response;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemResponse {
    private Long id;
    private CartResponse cart;
    private PizzaResponse pizza;
    private DrinkResponse drink;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private boolean isActive;
}

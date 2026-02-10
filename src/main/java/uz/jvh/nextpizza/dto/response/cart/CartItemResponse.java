package uz.jvh.nextpizza.dto.response.cart;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItemResponse {
    private Long id;
    private Long pizzaId;
    private String pizzaName;
    private Long drinkId;
    private String drinkName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private boolean isActive;
}

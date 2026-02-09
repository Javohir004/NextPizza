package uz.jvh.nextpizza.dto.response;

import lombok.*;


import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemResponse {

    private OrderResponse orderResponse;  // Qaysi buyurtmaga tegishli

    private PizzaResponse pizzaResponse;  // Agar pizza bo'lsa

    private DrinkResponse drinkResponse;  // Agar drink bo'lsa

    private String productName;  // Mahsulot nomi (copy - o'zgarsa ham saqlanadi)

    private Integer quantity;  // Miqdor

    private BigDecimal price;  // Buyurtma vaqtidagi narx (copy!)

    private BigDecimal totalPrice;
}

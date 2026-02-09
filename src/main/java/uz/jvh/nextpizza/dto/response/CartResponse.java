package uz.jvh.nextpizza.dto.response;

import lombok.*;
import uz.jvh.nextpizza.entity.CartItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartResponse {
    private Long id;
    private Long userId;
    private List<CartItemResponse> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private Integer totalItems = 0;
    private boolean isActive;
}

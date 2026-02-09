package uz.jvh.nextpizza.dto.request;


import lombok.*;
import uz.jvh.nextpizza.dto.response.UserResponse;
import uz.jvh.nextpizza.entity.CartItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartRequest {

    private UserResponse user;

    private List<CartItem> items = new ArrayList<>();

    private BigDecimal totalPrice = BigDecimal.ZERO;
    private Integer totalItems = 0;
}

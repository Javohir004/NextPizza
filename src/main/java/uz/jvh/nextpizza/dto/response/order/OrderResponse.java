package uz.jvh.nextpizza.dto.response.order;

import lombok.*;
import uz.jvh.nextpizza.dto.response.PizzaResponse;
import uz.jvh.nextpizza.dto.response.UserResponse;
import uz.jvh.nextpizza.enomerator.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private String userFullName;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private Integer totalItems;
    private String deliveryAddress;
    private List<OrderItemResponse> orderItems;

}

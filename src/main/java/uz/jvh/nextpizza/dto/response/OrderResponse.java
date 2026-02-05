package uz.jvh.nextpizza.dto.response;

import lombok.*;
import uz.jvh.nextpizza.entity.Pizza;
import uz.jvh.nextpizza.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {

    private Long id;
    private LocalDateTime orderDate;
    private double price;
    private User user;
    private List<Pizza> pizzas;
    private String deliveryAddress;

}

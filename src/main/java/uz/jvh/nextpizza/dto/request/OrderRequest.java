package uz.jvh.nextpizza.dto.request;

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
public class OrderRequest {
    private LocalDateTime orderDate;
    private double price;
    private User user;
    private List<Pizza> pizzas;
    private String deliveryAddress;
}


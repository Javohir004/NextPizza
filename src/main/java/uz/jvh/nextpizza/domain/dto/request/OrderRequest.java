package uz.jvh.nextpizza.domain.dto.request;

import lombok.*;
import uz.jvh.nextpizza.domain.entity.Address;
import uz.jvh.nextpizza.domain.entity.Pizza;
import uz.jvh.nextpizza.domain.entity.User;

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
    private Address deliveryAddress;
}


package uz.jvh.nextpizza.dto.response;

import lombok.*;
import uz.jvh.nextpizza.entity.Address;
import uz.jvh.nextpizza.entity.Pizza;
import uz.jvh.nextpizza.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {

    private UUID id;
    private LocalDateTime orderDate;
    private double price;
    private User user;
    private List<Pizza> pizzas;
    private Address deliveryAddress;

}

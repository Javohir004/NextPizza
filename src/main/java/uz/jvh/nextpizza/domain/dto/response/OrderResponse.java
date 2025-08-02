package uz.jvh.nextpizza.domain.dto.response;

import lombok.*;
import uz.jvh.nextpizza.domain.entity.Address;
import uz.jvh.nextpizza.domain.entity.Food;
import uz.jvh.nextpizza.domain.entity.User;

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
    private List<Food> foods;
    private Address deliveryAddress;

}

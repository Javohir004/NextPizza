package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order extends BaseEntity {

    private int quantity;

    private LocalDateTime orderDate;

    private double price;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;



    @ManyToMany
    @JoinTable(
            name = "ordered_food",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foods;
}

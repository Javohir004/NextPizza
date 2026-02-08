package uz.jvh.nextpizza.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.math.BigDecimal;


@Entity(name = "cart_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItem extends BaseEntity {
    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Pizza pizza;

    @ManyToOne
    private Drink drink;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}

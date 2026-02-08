package uz.jvh.nextpizza.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;  // Qaysi buyurtmaga tegishli

    @ManyToOne
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;  // Agar pizza bo'lsa

    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drink drink;  // Agar drink bo'lsa

    @Column(name = "product_name", nullable = false)
    private String productName;  // Mahsulot nomi (copy - o'zgarsa ham saqlanadi)

    @Column(nullable = false)
    private Integer quantity;  // Miqdor

    @Column(name = "price", nullable = false)
    private BigDecimal price;  // Buyurtma vaqtidagi narx (copy!)

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
}

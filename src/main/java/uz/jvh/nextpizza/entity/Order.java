package uz.jvh.nextpizza.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.jvh.nextpizza.enomerator.OrderState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Kimning buyurtmasi

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();  // ← Buyurtmadagi mahsulotlar

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state", nullable = false)
    private OrderState orderState;  // CREATED, COOKING, DELIVERING, COMPLETED

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;  // Jami narx

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "total_items")
    private Integer totalItems;
}

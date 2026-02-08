package uz.jvh.nextpizza.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cart extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;  // Kimning savati (har bir user uchun 1 ta cart)

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();  // Savatdagi mahsulotlar

    @Column(name = "total_price")
    private BigDecimal totalPrice = BigDecimal.ZERO;  // Jami narx

    @Column(name = "total_items")
    private Integer totalItems = 0;
}

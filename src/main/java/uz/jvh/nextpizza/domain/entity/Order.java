package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.jvh.nextpizza.domain.enomerator.DrinkType;

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


    private LocalDateTime orderDate;

    private double price;


    /** CascadeType.ALL esa boshqa operatsiyalarni ham (update, delete va boshqalar) User ga uzatadi,
     *  bu xavfli, chunki Order o‘chirganda foydalanuvchi ham o‘chib ketishi mumkin.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;



    @ManyToMany
    @JoinTable(
            name = "ordered_food",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Pizza> pizzas;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Drinks> drinks;

    @OneToOne(cascade = CascadeType.ALL)
    private Address deliveryAddress;
}

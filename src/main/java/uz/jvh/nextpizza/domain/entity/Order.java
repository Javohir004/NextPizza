package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order extends BaseEntity {

    private int quantity;

    private LocalDateTime orderDate;

    private double price;

}

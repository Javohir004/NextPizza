package uz.jvh.nextpizza.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity(name = "orderHistory")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderHistory extends BaseEntity {

}

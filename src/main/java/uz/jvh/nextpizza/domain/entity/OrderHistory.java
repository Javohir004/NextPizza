package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity(name = "orderHistory")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderHistory extends BaseEntity {

}

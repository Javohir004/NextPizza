package uz.jvh.nextpizza.domain.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity(name = "history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class History extends BaseEntity {

}

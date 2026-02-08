package uz.jvh.nextpizza.repository;

import uz.jvh.nextpizza.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}

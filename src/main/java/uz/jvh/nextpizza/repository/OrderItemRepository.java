package uz.jvh.nextpizza.repository;

import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}

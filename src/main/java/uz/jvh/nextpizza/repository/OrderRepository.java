package uz.jvh.nextpizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.enomerator.OrderStatus;
import uz.jvh.nextpizza.entity.Order;
import uz.jvh.nextpizza.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByOrderDateDesc(User user);

    List<Order> findAllByOrderByOrderDateDesc();

    List<Order> findByOrderStatusOrderByOrderDateDesc(OrderStatus status);

}

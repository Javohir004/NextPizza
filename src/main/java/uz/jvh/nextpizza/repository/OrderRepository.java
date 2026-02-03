package uz.jvh.nextpizza.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.entity.Order;

import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {


}

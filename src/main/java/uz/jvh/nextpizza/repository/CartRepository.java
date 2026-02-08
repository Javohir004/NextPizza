package uz.jvh.nextpizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}

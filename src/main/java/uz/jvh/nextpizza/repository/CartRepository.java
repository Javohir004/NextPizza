package uz.jvh.nextpizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.entity.Cart;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Boolean existsByUserIdAndIsActiveTrue(Long userId);

    Optional<Cart> findByUserIdAndIsActiveTrue(Long userId);
}

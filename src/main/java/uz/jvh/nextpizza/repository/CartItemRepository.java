package uz.jvh.nextpizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.entity.Cart;
import uz.jvh.nextpizza.entity.CartItem;
import uz.jvh.nextpizza.entity.Drink;
import uz.jvh.nextpizza.entity.Pizza;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndPizza(Cart cart, Pizza pizza);

    Optional<CartItem> findByCartAndDrink(Cart cart, Drink drink);
}

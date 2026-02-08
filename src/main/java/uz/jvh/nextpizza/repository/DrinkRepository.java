package uz.jvh.nextpizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.enomerator.DrinkType;
import uz.jvh.nextpizza.entity.Drink;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {

    boolean existsByDrinkNameAndDrinkTypeAndVolume(String name , DrinkType drinkType, Double volume);
}

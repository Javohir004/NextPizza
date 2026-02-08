package uz.jvh.nextpizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.enomerator.DrinkType;
import uz.jvh.nextpizza.entity.Drink;

import java.util.List;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long> {

    boolean existsByDrinkNameAndDrinkTypeAndVolumeAndIsActiveTrue(String drinkName, DrinkType drinkType, Double volume);

    List<Drink> findAllByIsActiveTrue();

    List<Drink> findByDrinkTypeAndIsActiveTrue(DrinkType drinkType);

}

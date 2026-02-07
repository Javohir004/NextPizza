package uz.jvh.nextpizza.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.enomerator.PizzaType;
import uz.jvh.nextpizza.entity.Pizza;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {


    @Query("SELECT f FROM pizzas f WHERE " +
            "(:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:minPrice IS NULL OR f.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR f.price <= :maxPrice) AND " +
            "(:foodType IS NULL OR f.pizzaType = :foodType)")
    List<Pizza> searchFoods(@Param("name") String name,
                            @Param("minPrice") BigDecimal minPrice,
                            @Param("maxPrice") BigDecimal maxPrice,
                            @Param("foodType") PizzaType pizzaType);

    // Type va nom bo'yicha saralangan
    List<Pizza> findAllByIsActiveTrueOrderByPizzaTypeAscNameAsc();

    // Type va narx bo'yicha
    List<Pizza> findAllByIsActiveTrueOrderByPizzaTypeAscPriceAsc();

    // Bitta type bo'yicha
    List<Pizza> findAllByIsActiveTrueAndPizzaTypeOrderByPriceAsc(PizzaType type);

}

package uz.jvh.nextpizza.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.domain.enomerator.FoodType;
import uz.jvh.nextpizza.domain.entity.Pizza;

import java.util.List;
import java.util.UUID;

@Repository
public interface FoodRepository extends JpaRepository<Pizza, UUID> {


    @Query("SELECT f FROM foods f WHERE " +
            "(:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:minPrice IS NULL OR f.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR f.price <= :maxPrice) AND " +
            "(:foodType IS NULL OR f.foodType = :foodType)")
    List<Pizza> searchFoods(@Param("name") String name,
                            @Param("minPrice") Double minPrice,
                            @Param("maxPrice") Double maxPrice,
                            @Param("foodType") FoodType foodType);


}

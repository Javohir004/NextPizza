package uz.jvh.nextpizza.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.domain.entity.Food;

import java.util.UUID;

@Repository
public interface FoodRepository extends JpaRepository<Food, UUID> {



}

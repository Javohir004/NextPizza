package uz.jvh.nextpizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.entity.Drinks;

@Repository
public interface DrinkRepository extends JpaRepository<Drinks, Long> {

}

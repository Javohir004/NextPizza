package uz.jvh.nextpizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.domain.enomerator.UserRole;
import uz.jvh.nextpizza.domain.entity.User;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {


    List<User> findByRoleAndIsActiveTrueOrderByCreatedDesc(UserRole role);


    Optional<User> findByUsernameAndIsActiveTrue(String username);


    List<User> findAllByIsActiveTrueOrderByCreatedDesc();


}

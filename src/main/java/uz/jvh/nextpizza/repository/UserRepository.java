package uz.jvh.nextpizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.jvh.nextpizza.enomerator.Role;
import uz.jvh.nextpizza.entity.User;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByRoleAndIsActiveTrueOrderByCreatedDesc(Role role);

    Optional<User> findByFirstNameAndIsActiveTrue(String username);

    List<User> findAllByIsActiveTrueOrderByCreatedDesc();

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findUserByFirstNameAndIsActiveTrue(String firstName);

    Optional<User> findByIdAndIsActiveTrue(Long id);

    long countByIsActiveTrueAndRole(Role role);
}

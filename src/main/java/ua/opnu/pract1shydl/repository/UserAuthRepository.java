package ua.opnu.pract1shydl.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.opnu.pract1shydl.model.UserAuth;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

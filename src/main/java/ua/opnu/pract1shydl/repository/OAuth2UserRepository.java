package ua.opnu.pract1shydl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.pract1shydl.model.MyOAuth2User;

import java.util.Optional;

public interface OAuth2UserRepository extends JpaRepository<MyOAuth2User, Long> {
    Optional<MyOAuth2User> findByEmail(String email);
}
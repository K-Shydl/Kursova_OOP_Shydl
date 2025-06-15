package ua.opnu.pract1shydl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.pract1shydl.model.User;

public interface UserRepository extends JpaRepository<User, Long> {}
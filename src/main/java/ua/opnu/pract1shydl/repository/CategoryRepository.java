package ua.opnu.pract1shydl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.pract1shydl.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
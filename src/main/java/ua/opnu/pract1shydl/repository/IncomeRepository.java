package ua.opnu.pract1shydl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.pract1shydl.model.Income;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
  List<Income> findByUserId(Long userId);
}
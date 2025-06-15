package ua.opnu.pract1shydl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opnu.pract1shydl.model.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
  Optional<Budget> findByUserIdAndMonth(Long userId, String month);
  List<Budget> findByUserId(Long userId);
}
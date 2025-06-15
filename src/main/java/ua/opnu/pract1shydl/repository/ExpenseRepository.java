package ua.opnu.pract1shydl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.opnu.pract1shydl.model.Expense;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
  List<Expense> findByUserId(Long userId);
  List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
  @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND FUNCTION('to_char', e.date, 'YYYY-MM') = :month")
  List<Expense> findByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);

  List<Expense> findByUserIdAndDate(Long userId, LocalDate date);
}
package ua.opnu.pract1shydl.controller;

import ua.opnu.pract1shydl.dto.CategoryReportDTO;
import ua.opnu.pract1shydl.model.Expense;
import ua.opnu.pract1shydl.service.BudgetService;
import ua.opnu.pract1shydl.service.ExpenseService;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
  @RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

  private final ExpenseService expenseService;
  private final BudgetService budgetService;

  // 19. Перевищення бюджету
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/budget-exceeded")
  public boolean isBudgetExceeded(@RequestParam Long userId, @RequestParam String month) {
    return budgetService.isBudgetExceeded(userId, month);
  }

  // 20. Звіт по категоріях
  @GetMapping("/category-report")
  @PreAuthorize("hasRole('USER')")
  public List<CategoryReportDTO> getCategoryReport(@RequestParam Long userId, @RequestParam String month) {
    return expenseService.getCategoryReport(userId, month);
  }

  // 21. Сума витрат за місяць
  @GetMapping("/total-month")
  @PreAuthorize("hasRole('USER')")
  public BigDecimal getTotalExpenses(@RequestParam Long userId, @RequestParam String month) {
    return expenseService.getTotalExpensesForMonth(userId, month);
  }

  // 22. Середня витрата на день
  @GetMapping("/average-daily")
  @PreAuthorize("hasRole('USER')")
  public BigDecimal getAverageDailyExpense(@RequestParam Long userId, @RequestParam String month) {
    return expenseService.getAverageDailyExpense(userId, month);
  }

  // 23. Найбільші витрати місяця
  @GetMapping("/top-expenses")
  @PreAuthorize("hasRole('USER')")
  public List<Expense> getTopExpenses(@RequestParam Long userId, @RequestParam String month, @RequestParam int limit) {
    return expenseService.getTopExpenses(userId, month, limit);
  }

  // 24. Залишок бюджету
  @GetMapping("/remaining-budget")
  @PreAuthorize("hasRole('USER')")
  public BigDecimal getRemainingBudget(@RequestParam Long userId, @RequestParam String month) {
    return budgetService.getRemainingBudget(userId, month);
  }

  // 25. Витрати по даті
  @GetMapping("/by-date")
  @PreAuthorize("hasRole('USER')")
  public List<Expense> getExpensesByDate(@RequestParam Long userId, @RequestParam String date) {
    return expenseService.getExpensesByDate(userId, LocalDate.parse(date));
  }
}
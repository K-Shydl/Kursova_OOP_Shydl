package ua.opnu.pract1shydl.service;

import lombok.RequiredArgsConstructor;
import ua.opnu.pract1shydl.dto.BudgetDTO;
import ua.opnu.pract1shydl.model.Budget;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.repository.BudgetRepository;
import ua.opnu.pract1shydl.repository.ExpenseRepository;
import ua.opnu.pract1shydl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
  @Autowired
  private BudgetRepository budgetRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ExpenseRepository expenseRepository;

  public Budget createBudget(BudgetDTO dto) {
    User user = userRepository.findById(dto.getUserId()).orElseThrow();

    Budget budget = new Budget();
    budget.setTotalAmount(dto.getTotalAmount());
    budget.setMonth(dto.getMonth());
    budget.setUser(user);

    return budgetRepository.save(budget);
  }

  public List<Budget> getBudgets(Long userId) { return budgetRepository.findByUserId(userId); }
  public Budget updateBudget(Long id, Budget updated) {
    return budgetRepository.findById(id).map(b -> {
      b.setMonth(updated.getMonth());
      b.setTotalAmount(updated.getTotalAmount());
      return budgetRepository.save(b);
    }).orElse(null);
  }

  public boolean isBudgetExceeded(Long userId, String month) {
    BigDecimal totalExpenses = expenseRepository.findByUserIdAndMonth(userId, month).stream()
        .map(e -> BigDecimal.valueOf(e.getAmount()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal limit = budgetRepository.findByUserIdAndMonth(userId, month)
        .map(e -> BigDecimal.valueOf(e.getTotalAmount()))
        //.map(Budget::getTotalAmount)
        .orElse(BigDecimal.ZERO);

    return totalExpenses.compareTo(limit) > 0;
  }

  public BigDecimal getRemainingBudget(Long userId, String month) {
    BigDecimal limit = budgetRepository.findByUserIdAndMonth(userId, month)
        .map(e -> BigDecimal.valueOf(e.getTotalAmount()))
        //.map(Budget::getTotalAmount)
        .orElse(BigDecimal.ZERO);

    BigDecimal spent = expenseRepository.findByUserIdAndMonth(userId, month).stream()
        .map(e -> BigDecimal.valueOf(e.getAmount()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return limit.subtract(spent);
  }

  //public List<Budget> getAllBudgets() {
  //  return budgetRepository.findAll();
  //}
}
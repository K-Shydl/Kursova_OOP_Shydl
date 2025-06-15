package ua.opnu.pract1shydl.service;

import lombok.RequiredArgsConstructor;
import ua.opnu.pract1shydl.dto.ExpenseDTO;
import ua.opnu.pract1shydl.model.Category;
import ua.opnu.pract1shydl.model.Expense;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.repository.CategoryRepository;
import ua.opnu.pract1shydl.dto.CategoryReportDTO;
import ua.opnu.pract1shydl.repository.ExpenseRepository;
import ua.opnu.pract1shydl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
  @Autowired
  private ExpenseRepository expenseRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public Expense createExpense(ExpenseDTO dto) {
    User user = userRepository.findById(dto.getUserId()).orElseThrow();
    Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();

    Expense expense = new Expense();
    expense.setAmount(dto.getAmount());
    expense.setDescription(dto.getDescription());
    expense.setDate(dto.getDate());
    expense.setUser(user);
    expense.setCategory(category);

    return expenseRepository.save(expense);
  }

  //public List<Expense> getAllExpenses() {
  //  return expenseRepository.findAll();
  //}

  public List<Expense> getExpenses(Long userId) {
    return expenseRepository.findByUserId(userId); //{
  //    expense.getAmount(dto.getAmount());
  //    expense.getDescription(dto.getDescription());
  //    expense.getDate(dto.getDate());
  //    expense.getUser(user);
  //    expense.getCategory(category);
  //  }
  }
  //public List<Expense> getExpenses(Long userId, LocalDate startDate, LocalDate endDate) {
  //  return expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

  public Expense updateExpense(Long id, ExpenseDTO dto) {
    Expense expense = expenseRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Expense not found"));

    User user = userRepository.findById(dto.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found"));

    Category category = categoryRepository.findById(dto.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));

    expense.setAmount(dto.getAmount());
    expense.setDate(dto.getDate());
    expense.setDescription(dto.getDescription());
    expense.setUser(user);
    expense.setCategory(category);

    return expenseRepository.save(expense);
  }//).orElse(null);

  public void deleteExpense(Long id) { expenseRepository.deleteById(id); }

  public List<CategoryReportDTO> getCategoryReport(Long userId, String month) {
    List<Expense> expenses = expenseRepository.findByUserIdAndMonth(userId, month);
    return expenses.stream()
        .collect(Collectors.groupingBy(
            e -> e.getCategory().getName(),
            Collectors.mapping(e -> BigDecimal.valueOf(e.getAmount()), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
        ))
        .entrySet().stream()
        .map(entry -> {
          CategoryReportDTO dto = new CategoryReportDTO();
          dto.setCategoryName(entry.getKey());
          dto.setTotalSpent(entry.getValue());
          return dto;
        })
        .toList();
  }

  public BigDecimal getTotalExpensesForMonth(Long userId, String month) {
    return expenseRepository.findByUserIdAndMonth(userId, month).stream()
        .map(e -> BigDecimal.valueOf(e.getAmount()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal getAverageDailyExpense(Long userId, String month) {
    List<Expense> expenses = expenseRepository.findByUserIdAndMonth(userId, month);
    long days = expenses.stream().map(Expense::getDate).collect(Collectors.toSet()).size();
    if (days == 0) return BigDecimal.ZERO;
    BigDecimal total = expenses.stream().map(e -> BigDecimal.valueOf(e.getAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
    return total.divide(BigDecimal.valueOf(days), RoundingMode.HALF_UP);
  }

  public List<Expense> getTopExpenses(Long userId, String month, int limit) {
    return expenseRepository.findByUserIdAndMonth(userId, month).stream()
        .sorted((a, b) -> Double.compare(b.getAmount(), a.getAmount()))
        .limit(limit)
        .toList();
  }

  public List<Expense> getExpensesByDate(Long userId, LocalDate date) {
    return expenseRepository.findByUserIdAndDate(userId, date);
  }
}
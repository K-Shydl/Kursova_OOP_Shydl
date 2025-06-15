package ua.opnu.pract1shydl.controller;

import jakarta.persistence.JoinColumn;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ua.opnu.pract1shydl.dto.ExpenseDTO;
import ua.opnu.pract1shydl.model.Expense;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.repository.ExpenseRepository;
import ua.opnu.pract1shydl.repository.CategoryRepository;
import ua.opnu.pract1shydl.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

  private ExpenseRepository expenseRepository;
  private CategoryRepository categoryRepository;

  //@JoinColumn(name = "user_id")
  //private UserId userId;

  @Autowired
  private ExpenseService expenseService;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public Expense create(@RequestBody ExpenseDTO dto) {
    return expenseService.createExpense(dto);
  }

  //@GetMapping
  //public List<Expense> list() {
  //  return expenseService.getAllExpenses();
  //}
  @GetMapping("/user/{userId}")
  @PreAuthorize("hasRole('USER')")
  public List<Expense> getExpenses(@PathVariable Long userId) {
    return expenseService.getExpenses(userId);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public Expense update(@PathVariable Long id, @RequestBody ExpenseDTO dto) {
    return expenseService.updateExpense(id, dto);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public void deleteExpense(@PathVariable Long id) { expenseService.deleteExpense(id); }
}
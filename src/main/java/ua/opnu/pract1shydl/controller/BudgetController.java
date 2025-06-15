package ua.opnu.pract1shydl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ua.opnu.pract1shydl.dto.BudgetDTO;
import ua.opnu.pract1shydl.model.Budget;
import ua.opnu.pract1shydl.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

  @Autowired
  private BudgetService budgetService;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public Budget create(@RequestBody BudgetDTO dto) {
    return budgetService.createBudget(dto);
  }

  @GetMapping("/user/{userId}")
  @PreAuthorize("hasRole('USER')")
  public List<Budget> getBudgets(@PathVariable Long userId) {
    return budgetService.getBudgets(userId);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public Budget updateBudget(@PathVariable Long id, @RequestBody Budget budget) {
    return budgetService.updateBudget(id, budget);
  }

  //@GetMapping
  //@PreAuthorize("hasRole('USER')")
  //public List<Budget> list() {
  //  return budgetService.getAllBudgets();
  //}
}
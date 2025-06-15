package ua.opnu.pract1shydl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ua.opnu.pract1shydl.dto.IncomeDTO;
import ua.opnu.pract1shydl.model.Income;
import ua.opnu.pract1shydl.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {

  @Autowired
  private IncomeService incomeService;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public Income create(@RequestBody IncomeDTO dto) {
    return incomeService.createIncome(dto);
  }

  //@GetMapping
  //public List<Income> list() {
  //  return incomeService.getAllIncomes();
  //}

  @GetMapping("/user/{userId}")
  @PreAuthorize("hasRole('USER')")
  public List<Income> getIncomes(@PathVariable Long userId) {
    return incomeService.getIncomes(userId);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public void deleteIncome(@PathVariable Long id) {
    incomeService.deleteIncome(id);
  }
}
package ua.opnu.pract1shydl.service;

import lombok.RequiredArgsConstructor;
import ua.opnu.pract1shydl.dto.IncomeDTO;
import ua.opnu.pract1shydl.model.Income;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.repository.IncomeRepository;
import ua.opnu.pract1shydl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
  @Autowired
  private IncomeRepository incomeRepository;

  @Autowired
  private UserRepository userRepository;

  public Income createIncome(IncomeDTO dto) {
    User user = userRepository.findById(dto.getUserId()).orElseThrow();

    Income income = new Income();
    income.setAmount(dto.getAmount());
    income.setSource(dto.getSource());
    income.setDate(dto.getDate());
    income.setUser(user);

    return incomeRepository.save(income);
  }

  public List<Income> getIncomes(Long userId) { return incomeRepository.findByUserId(userId); }
  public void deleteIncome(Long id) { incomeRepository.deleteById(id); }

  //public List<Income> getAllIncomes() {
  //  return incomeRepository.findAll();
  //}
}
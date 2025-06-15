package ua.opnu.pract1shydl.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ua.opnu.pract1shydl.dto.BudgetDTO;
import ua.opnu.pract1shydl.model.Budget;
import ua.opnu.pract1shydl.model.Expense;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.repository.BudgetRepository;
import ua.opnu.pract1shydl.repository.ExpenseRepository;
import ua.opnu.pract1shydl.repository.UserRepository;
import ua.opnu.pract1shydl.service.BudgetService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private BudgetService budgetService;

    private BudgetDTO budgetDTO;
    private Budget budget;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");

        budgetDTO = new BudgetDTO();
        budgetDTO.setTotalAmount(1000.00);
        budgetDTO.setMonth("2023-08");
        budgetDTO.setUserId(1L);

        budget = new Budget();
        budget.setId(1L);
        budget.setTotalAmount(1000.00);
        budget.setMonth("2023-08");
        budget.setUser(user);
    }

    @Test
    public void testCreateBudget() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        // Act
        Budget createdBudget = budgetService.createBudget(budgetDTO);

        // Assert
        assertNotNull(createdBudget);
        assertEquals(1000.00, createdBudget.getTotalAmount());
        assertEquals("2023-08", createdBudget.getMonth());
        assertEquals(user, createdBudget.getUser());
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    public void testGetBudgets() {
        // Arrange
        when(budgetRepository.findByUserId(1L)).thenReturn(Arrays.asList(budget));

        // Act
        List<Budget> budgets = budgetService.getBudgets(1L);

        // Assert
        assertNotNull(budgets);
        assertEquals(1, budgets.size());
        assertEquals("2023-08", budgets.get(0).getMonth());
    }

    @Test
    public void testUpdateBudget() {
        // Arrange
        Budget updatedBudget = new Budget();
        updatedBudget.setTotalAmount(1200.00);
        updatedBudget.setMonth("2023-09");

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(updatedBudget);

        // Act
        Budget result = budgetService.updateBudget(1L, updatedBudget);

        // Assert
        assertNotNull(result);
        assertEquals(1200.00, result.getTotalAmount());
        assertEquals("2023-09", result.getMonth());
    }

    @Test
    public void testUpdateBudgetNotFound() {
        // Arrange
        Budget updatedBudget = new Budget();
        updatedBudget.setTotalAmount(1200.00);
        updatedBudget.setMonth("2023-09");

        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Budget result = budgetService.updateBudget(1L, updatedBudget);

        // Assert
        assertNull(result);
    }

    

    @Test
    public void testGetRemainingBudget() {
        // Arrange
        when(expenseRepository.findByUserIdAndMonth(1L, "2023-08"))
                .thenReturn(Arrays.asList(
                        new Expense(1L, 200.00, "Expense 1", LocalDate.now(), user, null),
                        new Expense(2L, 300.00, "Expense 2", LocalDate.now(), user, null)
                ));
        when(budgetRepository.findByUserIdAndMonth(1L, "2023-08")).thenReturn(Optional.of(budget));

        // Act
        BigDecimal remaining = budgetService.getRemainingBudget(1L, "2023-08");

        // Assert
        assertEquals(BigDecimal.valueOf(500.00), remaining);
    }
}


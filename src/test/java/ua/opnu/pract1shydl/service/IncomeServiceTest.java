package ua.opnu.pract1shydl.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ua.opnu.pract1shydl.dto.IncomeDTO;
import ua.opnu.pract1shydl.model.Income;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.repository.IncomeRepository;
import ua.opnu.pract1shydl.repository.UserRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class IncomeServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IncomeService incomeService;

    private IncomeDTO incomeDTO;
    private Income income;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");

        incomeDTO = new IncomeDTO();
        incomeDTO.setAmount(1000.00);
        incomeDTO.setSource("Job");
        incomeDTO.setDate(LocalDate.now());
        incomeDTO.setUserId(1L);

        income = new Income();
        income.setId(1L);
        income.setAmount(1000.00);
        income.setSource("Job");
        income.setDate(LocalDate.now());
        income.setUser(user);
    }

    @Test
    public void testCreateIncome() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(incomeRepository.save(any(Income.class))).thenReturn(income);

        // Act
        Income createdIncome = incomeService.createIncome(incomeDTO);

        // Assert
        assertNotNull(createdIncome);
        assertEquals(1000.00, createdIncome.getAmount());
        assertEquals("Job", createdIncome.getSource());
        assertEquals(user, createdIncome.getUser());
        verify(incomeRepository, times(1)).save(any(Income.class));
    }

    @Test
    public void testCreateIncome_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> incomeService.createIncome(incomeDTO));
    }

    @Test
    public void testGetIncomes() {
        // Arrange
        when(incomeRepository.findByUserId(1L)).thenReturn(Arrays.asList(income));

        // Act
        List<Income> incomes = incomeService.getIncomes(1L);

        // Assert
        assertNotNull(incomes);
        assertEquals(1, incomes.size());
        assertEquals("Job", incomes.get(0).getSource());
    }

    @Test
    public void testDeleteIncome() {
        // Act
        incomeService.deleteIncome(1L);

        // Assert
        verify(incomeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteIncome_NotFound() {
        // Arrange
        doThrow(new IllegalArgumentException("Income not found")).when(incomeRepository).deleteById(1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> incomeService.deleteIncome(1L));
    }
}

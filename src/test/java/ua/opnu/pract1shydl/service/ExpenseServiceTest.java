package ua.opnu.pract1shydl.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ua.opnu.pract1shydl.dto.ExpenseDTO;
import ua.opnu.pract1shydl.dto.CategoryReportDTO;
import ua.opnu.pract1shydl.model.Category;
import ua.opnu.pract1shydl.model.Expense;
import ua.opnu.pract1shydl.model.User;
import ua.opnu.pract1shydl.repository.ExpenseRepository;
import ua.opnu.pract1shydl.repository.CategoryRepository;
import ua.opnu.pract1shydl.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private ExpenseDTO expenseDTO;
    private Expense expense;
    private User user;
    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");

        category = new Category();
        category.setId(1L);
        category.setName("Food");

        expenseDTO = new ExpenseDTO();
        expenseDTO.setAmount(100.00);
        expenseDTO.setDescription("Grocery");
        expenseDTO.setDate(LocalDate.now());
        expenseDTO.setUserId(1L);
        expenseDTO.setCategoryId(1L);

        expense = new Expense();
        expense.setId(1L);
        expense.setAmount(100.00);
        expense.setDescription("Grocery");
        expense.setDate(LocalDate.now());
        expense.setUser(user);
        expense.setCategory(category);
    }

    @Test
    public void testCreateExpense() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Act
        Expense createdExpense = expenseService.createExpense(expenseDTO);

        // Assert
        assertNotNull(createdExpense);
        assertEquals(100.00, createdExpense.getAmount());
        assertEquals("Grocery", createdExpense.getDescription());
        assertEquals(user, createdExpense.getUser());
        assertEquals(category, createdExpense.getCategory());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    public void testCreateExpense_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> expenseService.createExpense(expenseDTO));
    }

    @Test
    public void testCreateExpense_CategoryNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> expenseService.createExpense(expenseDTO));
    }

    @Test
    public void testGetExpenses() {
        // Arrange
        when(expenseRepository.findByUserId(1L)).thenReturn(Arrays.asList(expense));

        // Act
        List<Expense> expenses = expenseService.getExpenses(1L);

        // Assert
        assertNotNull(expenses);
        assertEquals(1, expenses.size());
        assertEquals("Grocery", expenses.get(0).getDescription());
    }

    @Test
    public void testUpdateExpense() {
        // Arrange
        ExpenseDTO updatedExpenseDTO = new ExpenseDTO();
        updatedExpenseDTO.setAmount(150.00);
        updatedExpenseDTO.setDescription("New Grocery");
        updatedExpenseDTO.setDate(LocalDate.now());
        updatedExpenseDTO.setUserId(1L);
        updatedExpenseDTO.setCategoryId(1L);

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Act
        Expense updatedExpense = expenseService.updateExpense(1L, updatedExpenseDTO);

        // Assert
        assertNotNull(updatedExpense);
        assertEquals(150.00, updatedExpense.getAmount());
        assertEquals("New Grocery", updatedExpense.getDescription());
    }

    @Test
    public void testDeleteExpense() {
        // Act
        expenseService.deleteExpense(1L);

        // Assert
        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetCategoryReport() {
        // Arrange
        Expense expense1 = new Expense();
        expense1.setAmount(50.00);
        expense1.setCategory(category);
        expense1.setUser(user);
        expense1.setDate(LocalDate.now());

        Expense expense2 = new Expense();
        expense2.setAmount(70.00);
        expense2.setCategory(category);
        expense2.setUser(user);
        expense2.setDate(LocalDate.now());

        when(expenseRepository.findByUserIdAndMonth(1L, "2023-08")).thenReturn(Arrays.asList(expense1, expense2));

        // Act
        List<CategoryReportDTO> report = expenseService.getCategoryReport(1L, "2023-08");

        // Assert
        assertNotNull(report);
        assertEquals(1, report.size());
        assertEquals("Food", report.get(0).getCategoryName());
        assertEquals(BigDecimal.valueOf(120.00), report.get(0).getTotalSpent());
    }

    @Test
    public void testGetTotalExpensesForMonth() {
        // Arrange
        Expense expense1 = new Expense();
        expense1.setAmount(50.00);
        expense1.setCategory(category);
        expense1.setUser(user);
        expense1.setDate(LocalDate.now());

        Expense expense2 = new Expense();
        expense2.setAmount(70.00);
        expense2.setCategory(category);
        expense2.setUser(user);
        expense2.setDate(LocalDate.now());

        when(expenseRepository.findByUserIdAndMonth(1L, "2023-08")).thenReturn(Arrays.asList(expense1, expense2));

        // Act
        BigDecimal totalExpenses = expenseService.getTotalExpensesForMonth(1L, "2023-08");

        // Assert
        assertEquals(BigDecimal.valueOf(120.00), totalExpenses);
    }
}


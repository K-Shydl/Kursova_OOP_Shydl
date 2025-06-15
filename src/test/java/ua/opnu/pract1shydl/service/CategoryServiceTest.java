package ua.opnu.pract1shydl.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ua.opnu.pract1shydl.dto.CategoryDTO;
import ua.opnu.pract1shydl.model.Category;
import ua.opnu.pract1shydl.repository.CategoryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryDTO categoryDTO;
    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryDTO = new CategoryDTO();
        categoryDTO.setName("Food");
        categoryDTO.setType("Expense");

        category = new Category();
        category.setId(1L);
        category.setName("Food");
        category.setType("Expense");
    }

    @Test
    public void testCreateCategory() {
        // Arrange
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        Category createdCategory = categoryService.createCategory(categoryDTO);

        // Assert
        assertNotNull(createdCategory);
        assertEquals("Food", createdCategory.getName());
        assertEquals("Expense", createdCategory.getType());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testGetAllCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        // Act
        List<Category> categories = categoryService.getAllCategories();

        // Assert
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Food", categories.get(0).getName());
    }

    @Test
    public void testUpdateCategory() {
        // Arrange
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Food");
        updatedCategory.setType("Expense");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // Act
        Category result = categoryService.updateCategory(1L, updatedCategory);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Food", result.getName());
        assertEquals("Expense", result.getType());
    }

    @Test
    public void testUpdateCategoryNotFound() {
        // Arrange
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Food");
        updatedCategory.setType("Expense");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Category result = categoryService.updateCategory(1L, updatedCategory);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeleteCategory() {
        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}


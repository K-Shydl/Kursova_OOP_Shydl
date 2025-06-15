package ua.opnu.pract1shydl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import ua.opnu.pract1shydl.dto.CategoryDTO;
import ua.opnu.pract1shydl.model.Category;
import ua.opnu.pract1shydl.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public Category create(@RequestBody CategoryDTO dto) {
    return categoryService.createCategory(dto);
  }

  @GetMapping
  @PreAuthorize("hasRole('USER')")
  public List<Category> getAllCategories() {
    return categoryService.getAllCategories();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
    return categoryService.updateCategory(id, category);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('USER')")
  public void deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
  }
}
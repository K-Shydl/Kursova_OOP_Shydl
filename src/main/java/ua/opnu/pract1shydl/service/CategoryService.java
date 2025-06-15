package ua.opnu.pract1shydl.service;

import lombok.RequiredArgsConstructor;
import ua.opnu.pract1shydl.model.Category;
import ua.opnu.pract1shydl.repository.CategoryRepository;
import ua.opnu.pract1shydl.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
  @Autowired
  private CategoryRepository categoryRepository;

  public Category createCategory(CategoryDTO categoryDTO) {
    Category category = new Category();
    category.setName(categoryDTO.getName());
    category.setType(categoryDTO.getType());
    return categoryRepository.save(category);
  }

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  public Category updateCategory(Long id, Category updated) {
    return categoryRepository.findById(id).map(c -> {
      c.setName(updated.getName());
      c.setType(updated.getType());
      return categoryRepository.save(c);
    }).orElse(null);
  }

  public void deleteCategory(Long id) { categoryRepository.deleteById(id); }
}
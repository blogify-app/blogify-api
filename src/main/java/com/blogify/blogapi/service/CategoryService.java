package com.blogify.blogapi.service;

import com.blogify.blogapi.model.validator.CategoryValidator;
import com.blogify.blogapi.repository.CategoryRepository;
import com.blogify.blogapi.repository.model.Category;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryValidator categoryValidator;

  public List<Category> findAllByLabel(String label) {
    return categoryRepository.findAllByNameContainingIgnoreCase(label);
  }

  @Transactional
  public Category save(Category toSave) {
    return categoryRepository.save(toSave);
  }

  public List<Category> saveAll(List<Category> categories) {
    categoryValidator.accept(categories);
    return categoryRepository.saveAll(categories);
  }
}

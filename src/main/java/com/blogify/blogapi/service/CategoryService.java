package com.blogify.blogapi.service;

import com.blogify.blogapi.model.Category;
import com.blogify.blogapi.repository.CategoryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  @Transactional
  public Category save(Category toSave) {
    return categoryRepository.save(toSave);
  }
}

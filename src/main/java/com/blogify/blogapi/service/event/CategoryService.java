package com.blogify.blogapi.service.event;

import com.blogify.blogapi.repository.CategoryRepository;
import com.blogify.blogapi.repository.model.Category;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public List<Category> getAll() {
    return categoryRepository.findAll();
  }
}

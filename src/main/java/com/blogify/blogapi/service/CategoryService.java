package com.blogify.blogapi.service;

import com.blogify.blogapi.repository.CategoryRepository;
import com.blogify.blogapi.repository.UserRepository;
import com.blogify.blogapi.repository.model.Category;
import java.util.List;

import com.blogify.blogapi.repository.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  public Category findById(String id){return categoryRepository.getReferenceById(id);}

  @Transactional
  public Category save(Category toSave) {
    return categoryRepository.save(toSave);
  }
}

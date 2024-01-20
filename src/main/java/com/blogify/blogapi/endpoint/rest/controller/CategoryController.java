package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.model.Category;
import com.blogify.blogapi.service.CategoryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  //TODO: Should be mapped when JavaClient is correctly set
  @GetMapping(value = "/categories")
  public List<Category> send_emails() {
    return categoryService.findAll();
  }
}

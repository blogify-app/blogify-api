package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.CategoryMapper;
import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.service.CategoryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CategoryController {

  private final CategoryService service;

  private final CategoryMapper mapper;

  @GetMapping(value = "/categories")
  public List<Category> getCategories() {
    return service.findAll().stream()
            .map(mapper::toRest)
            .toList();
  }

  @GetMapping(value = "/categories/{cid}")
  public Category getCategoryById(
          @PathVariable("cid") String categoryId) {
    return mapper.toRest(service.findById(categoryId));
  }
}

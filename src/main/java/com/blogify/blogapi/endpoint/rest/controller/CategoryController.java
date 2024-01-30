package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.CategoryMapper;
import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.service.CategoryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
public class CategoryController {

  private final CategoryService service;

  private final CategoryMapper mapper;

  @GetMapping(value = "/categories")
  public List<Category> getCategories(
      @RequestParam(value = "label", required = false, defaultValue = "") String label) {
    return service.findAllByLabel(label).stream().map(mapper::toRest).toList();
  }
}

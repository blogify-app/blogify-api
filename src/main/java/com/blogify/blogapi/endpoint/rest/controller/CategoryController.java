package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.CategoryMapper;
import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.service.CategoryService;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  @PutMapping("/categories")
  public List<Category> crupdateCategories(@RequestBody List<Category> categories){
    return service.saveAll(categories.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toUnmodifiableList())).stream()
            .map(mapper::toRest)
            .collect(Collectors.toUnmodifiableList());
  }

}

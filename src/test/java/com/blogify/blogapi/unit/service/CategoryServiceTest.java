package com.blogify.blogapi.unit.service;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.repository.model.Category;
import com.blogify.blogapi.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.blogify.blogapi.integration.conf.MockData.category;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryServiceTest extends FacadeIT {
  @Autowired CategoryService subject;

  @Test
  void create_category_ok() {
    Category actual = subject.save(category());

    assertNotNull(actual);
    assertNotNull(actual.getId());
    assertNotNull(actual.getCreationDatetime());
  }
}

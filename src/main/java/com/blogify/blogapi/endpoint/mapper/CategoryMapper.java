package com.blogify.blogapi.endpoint.mapper;

import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.repository.model.PostCategory;
import com.blogify.blogapi.repository.model.UserCategory;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

  public Category toRest(UserCategory domain) {
    return toRest(domain.getCategory());
  }

  public Category toRest(PostCategory domain) {
    return toRest(domain.getCategory());
  }

  public PostCategory toPostCategoryDomain(Category rest) {
    return PostCategory.builder().id(UUID.randomUUID().toString()).category(toDomain(rest)).build();
  }

  public com.blogify.blogapi.repository.model.Category toDomain(Category rest) {
    return com.blogify.blogapi.repository.model.Category.builder()
        .id(rest.getId())
        .name(rest.getLabel())
        .build();
  }

  public Category toRest(com.blogify.blogapi.repository.model.Category domain) {
    return new Category().id(domain.getId()).label(domain.getName());
  }
}

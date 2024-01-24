package com.blogify.blogapi.endpoint.mapper;

import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.repository.model.UserCategory;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

  public Category toRest(UserCategory domain){
    return toRest(domain.getCategory());
  }

  public Category toRest(com.blogify.blogapi.repository.model.Category domain){
    return new Category()
        .id(domain.getId())
        .label(domain.getName());
  }
}

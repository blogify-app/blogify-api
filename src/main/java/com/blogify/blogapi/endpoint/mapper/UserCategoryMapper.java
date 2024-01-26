package com.blogify.blogapi.endpoint.mapper;

import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.repository.UserCategoryRepository;
import com.blogify.blogapi.repository.model.UserCategory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class UserCategoryMapper {
  private final UserCategoryRepository userCategoryRepository;
  public List<UserCategory> toDomain(List<Category> toCreate) {
    return toCreate.stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
  }

  public com.blogify.blogapi.repository.model.UserCategory toDomain(Category restCategory) {
    return UserCategory.builder()
        .id((userCategoryRepository.findUserCategoryByCategoryId(restCategory.getId())).getId())
        .category(com.blogify.blogapi.repository.model.Category.builder()
            .id(restCategory.getId())
            .name(restCategory.getLabel())
            .build())
        .build();
  }
}

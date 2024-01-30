package com.blogify.blogapi.endpoint.mapper;

import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.repository.model.UserCategory;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCategoryMapper {
  private final CategoryMapper categoryMapper;

  public List<UserCategory> toCategoryToUserCategory(
      com.blogify.blogapi.endpoint.rest.model.User user, User modelUser) {
    return user.getCategories().stream()
        .map(category -> toCategoryToUserCategory(category, modelUser))
        .toList();
  }

  public com.blogify.blogapi.repository.model.UserCategory toCategoryToUserCategory(
      Category category, User user) {
    return UserCategory.builder()
        .id(UUID.randomUUID().toString())
        .user(user)
        .category(categoryMapper.toDomain(category))
        .build();
  }
}

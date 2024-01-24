package com.blogify.blogapi.utils;

import static com.blogify.blogapi.model.enums.Role.CLIENT;
import static com.blogify.blogapi.model.enums.Sex.M;
import static java.util.UUID.randomUUID;

import com.blogify.blogapi.repository.model.Category;
import com.blogify.blogapi.repository.model.User;
import java.time.Instant;
import java.util.List;

public class TestUtils {
  public static User user() {
    String userId = randomUUID().toString();

    return User.builder()
        .id(userId)
        .mail("dummy@gmail.com")
        .firstname("John")
        .role(CLIENT)
        .userCategories(List.of())
        .sex(M)
        .lastname("Doe")
        .build();
  }

  public static User ignoreIdAnCreationDatetime(User user) {
    return user.toBuilder().id(null).creationDatetime(null).build();
  }

  public static List<User> ignoreIdsAndCreationDatetime(List<User> users) {
    return users.stream()
        .peek(
            user -> {
              user.setId(null);
              user.setCreationDatetime(null);
            })
        .toList();
  }

  public static Category category() {
    return Category.builder()
        .id(randomUUID().toString())
        .name("machine learning")
        .creationDatetime(Instant.now())
        .build();
  }
}

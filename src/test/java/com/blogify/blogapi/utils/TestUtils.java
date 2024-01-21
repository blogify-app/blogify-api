package com.blogify.blogapi.utils;

import com.blogify.blogapi.repository.model.User;
import java.util.List;

import static com.blogify.blogapi.model.enums.Role.CLIENT;
import static com.blogify.blogapi.model.enums.Sex.M;
import static java.util.UUID.randomUUID;

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
        .peek(user -> {user.setId(null); user.setCreationDatetime(null);}).toList();
  }

}

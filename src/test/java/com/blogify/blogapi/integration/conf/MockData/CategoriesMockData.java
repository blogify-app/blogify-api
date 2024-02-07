package com.blogify.blogapi.integration.conf.MockData;

import static java.util.UUID.randomUUID;

import com.blogify.blogapi.endpoint.rest.model.Category;
import java.time.Instant;

public class CategoriesMockData {
  public static final String CATEGORY1_ID = "category1_id";
  public static final String CATEGORY2_ID = "category2_id";
  public static final String CATEGORY1_LABEL = "mathematics";
  public static final String CATEGORY2_LABEL = "prog5";

  public static Category category1() {
    return new Category().id(CATEGORY1_ID).label(CATEGORY1_LABEL);
  }

  public static Category category2() {
    return new Category().id(CATEGORY2_ID).label(CATEGORY2_LABEL);
  }

  public static com.blogify.blogapi.repository.model.Category category() {
    return com.blogify.blogapi.repository.model.Category.builder()
        .id(randomUUID().toString())
        .name("machine learning")
        .creationDatetime(Instant.now())
        .build();
  }
}

package com.blogify.blogapi.integration.conf;

import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.endpoint.rest.model.Sex;
import com.blogify.blogapi.endpoint.rest.model.User;
import com.blogify.blogapi.endpoint.rest.model.UserStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class MockData {
  public static final String CLIENT1_ID = "client1_id";
  public static final String CLIENT2_ID = "client2_id";
  public static final String MANAGER1_ID = "manager1_id";
  public static final String CATEGORY1_ID = "category1_id";
  public static final String CATEGORY2_ID = "category2_id";
  public static final String CATEGORY1_LABEL = "mathematics";
  public static final String CATEGORY2_LABEL = "prog5";

  public static Category category1(){
    return new Category()
        .id(CATEGORY1_ID)
        .label(CATEGORY1_LABEL);
  }
  public static Category category2(){
    return new Category()
        .id(CATEGORY2_ID)
        .label(CATEGORY2_LABEL);
  }
  public static User client1() {
    return new User()
        .id(CLIENT1_ID)
        .firstName("Ryan")
        .lastName("Andria")
        .username("username_client1")
        .about("about_client1")
        .email("test+ryan@hei.school")
        .birthDate(LocalDate.parse("1995-01-01"))
        .sex(Sex.M)
        .status(UserStatus.ENABLED)
        .photoUrl("photo_url_client1")
        .bio("bio_client1")
        .profileBannerUrl("banner_url_client1")
        .entranceDatetime(Instant.parse("2000-01-01T08:12:20.00Z"))
        .categories(List.of(category1(),category2()))
        .isFollowed(null);
  }

  public static User client2() {
    return new User()
        .id(CLIENT2_ID)
        .firstName("Herilala")
        .lastName("Raf")
        .username("username_client2")
        .about("about_client2")
        .email("test+herilala@hei.school")
        .birthDate(LocalDate.parse("2002-01-01"))
        .sex(Sex.F)
        .status(UserStatus.ENABLED)
        .photoUrl("photo_url_client2")
        .bio("bio_client2")
        .profileBannerUrl("banner_url_client2")
        .entranceDatetime(Instant.parse("2002-01-01T08:12:20.00Z"))
        .categories(List.of(category1()))
        .isFollowed(null);
  }

  public static User manager1() {
    return new User()
        .id(MANAGER1_ID)
        .firstName("Vano")
        .lastName("Andria")
        .username("username_manager1")
        .about("about_manager1")
        .email("test+vano@hei.school")
        .birthDate(LocalDate.parse("2000-01-01"))
        .sex(Sex.M)
        .status(UserStatus.ENABLED)
        .photoUrl("photo_url_manager1")
        .bio("bio_manager1")
        .profileBannerUrl("banner_url_manager1")
        .entranceDatetime(Instant.parse("2000-09-01T08:12:20.00Z"))
        .categories(List.of())
        .isFollowed(null);
  }


}

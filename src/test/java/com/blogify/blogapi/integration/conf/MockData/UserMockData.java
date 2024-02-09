package com.blogify.blogapi.integration.conf.MockData;

import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY1_ID;
import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY1_LABEL;
import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY2_ID;
import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY2_LABEL;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_BANNER_URL;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_PROFILE_URL;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_BANNER_URL;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_PROFILE_URL;

import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.endpoint.rest.model.Sex;
import com.blogify.blogapi.endpoint.rest.model.SignUp;
import com.blogify.blogapi.endpoint.rest.model.User;
import com.blogify.blogapi.endpoint.rest.model.UserPicture;
import com.blogify.blogapi.endpoint.rest.model.UserPictureType;
import com.blogify.blogapi.endpoint.rest.model.UserStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class UserMockData {
  public static final String CLIENT1_ID = "client1_id";
  public static final String CLIENT2_ID = "client2_id";
  public static final String MANAGER1_ID = "manager1_id";

  public static final String CREATED_CLIENT_ID = "created_client_id";

  public static Category category1() {
    return new Category().id(CATEGORY1_ID).label(CATEGORY1_LABEL);
  }

  public static Category category2() {
    return new Category().id(CATEGORY2_ID).label(CATEGORY2_LABEL);
  }

  public static User client1() {
    return new User()
        .id(CLIENT1_ID)
        .firstName("Ryan")
        .lastName("Andria")
        .username("username_client1")
        .about("about_client1")
        .email("test@gmail.com")
        .birthDate(LocalDate.parse("1995-01-01"))
        .sex(Sex.M)
        .status(UserStatus.ENABLED)
        .photoUrl(CLIENT1_PROFILE_URL)
        .bio("bio_client1")
        .profileBannerUrl(null)
        .entranceDatetime(Instant.parse("2000-01-01T08:12:20.00Z"))
        .categories(List.of(category1(), category2()))
        .isFollowed(null);
  }

  public static User client2() {
    return new User()
        .id(CLIENT2_ID)
        .firstName("Herilala")
        .lastName("Raf")
        .username("username_client2")
        .about("about_client2")
        .email("hei.hajatiana@gmail.com")
        .birthDate(LocalDate.parse("2002-01-01"))
        .sex(Sex.F)
        .status(UserStatus.ENABLED)
        .photoUrl(null)
        .bio("bio_client2")
        .profileBannerUrl(null)
        .entranceDatetime(Instant.parse("2002-01-01T08:12:20.00Z"))
        .categories(List.of(category1()))
        .isFollowed(null);
  }

  public static User clientToCreate() {
    return new User()
        .id(CREATED_CLIENT_ID)
        .firstName("Amira")
        .lastName("Khan")
        .username("username_client3")
        .about("about_client3")
        .email("test+amira@hei.school")
        .birthDate(LocalDate.parse("1995-07-15"))
        .sex(Sex.M)
        .status(UserStatus.ENABLED)
        .photoUrl(null)
        .bio("bio_client3")
        .profileBannerUrl(null)
        .entranceDatetime(null)
        .categories(List.of(category2(), category1()))
        .isFollowed(true);
  }

  public static SignUp signUpToCreate() {
    return new SignUp()
        .id(CREATED_CLIENT_ID)
        .firstName(clientToCreate().getFirstName())
        .email(clientToCreate().getEmail())
        .sex(Sex.M)
        .status(UserStatus.ENABLED)
        .password("password")
        .providerId("providerId");
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
        .photoUrl(null)
        .bio("bio_manager1")
        .profileBannerUrl(null)
        .entranceDatetime(Instant.parse("2000-09-01T08:12:20.00Z"))
        .categories(List.of())
        .isFollowed(null);
  }

  public static UserPicture userPictureClient1Profile() {
    return new UserPicture()
        .userId(CLIENT1_ID)
        .type(UserPictureType.PROFILE)
        .url(CLIENT1_PROFILE_URL);
  }

  public static UserPicture userPictureClient2Profile() {
    return new UserPicture()
        .userId(CLIENT2_ID)
        .type(UserPictureType.PROFILE)
        .url(CLIENT2_PROFILE_URL);
  }

  public static UserPicture userPictureClient1Banner() {
    return new UserPicture()
        .userId(CLIENT1_ID)
        .type(UserPictureType.BANNER)
        .url(CLIENT1_BANNER_URL);
  }

  public static UserPicture userPictureClient2Banner() {
    return new UserPicture()
        .userId(CLIENT2_ID)
        .type(UserPictureType.BANNER)
        .url(CLIENT2_BANNER_URL);
  }

  public static com.blogify.blogapi.repository.model.User clientEntity1() {
    return com.blogify.blogapi.repository.model.User.builder()
        .id("CLIENT1_ID")
        .firstname("Ryan")
        .lastname("Andria")
        .username("username_client1")
        .about("about_client1")
        .mail("test@gmail.com")
        .birthdate(LocalDate.parse("1995-01-01"))
        .sex(com.blogify.blogapi.model.enums.Sex.M)
        .status(com.blogify.blogapi.model.enums.UserStatus.ENABLED)
        .photoKey(CLIENT1_PROFILE_URL) // Assuming CLIENT1_PROFILE_URL is defined somewhere
        .bio("bio_client1")
        .profileBannerKey(null)
        .creationDatetime(Instant.parse("2000-01-01T08:12:20.00Z"))
        .userCategories(List.of())
        .deleted(false)
        .build();
  }
}

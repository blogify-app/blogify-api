package com.blogify.blogapi.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.model.enums.Role;
import com.blogify.blogapi.model.enums.Sex;
import com.blogify.blogapi.model.enums.UserStatus;
import com.blogify.blogapi.model.validator.UserValidator;
import com.blogify.blogapi.repository.UserRepository;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.UserService;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserUnitTest extends FacadeIT {

  @Mock private UserRepository userRepository;

  @Mock private UserValidator userValidator;

  @InjectMocks private UserService userService;

  @Test
  void testGetterSetter() {
    User user = new User();

    // Test getter and setter for each field
    user.setId("1");
    assertEquals("1", user.getId());

    user.setFirstname("John");
    assertEquals("John", user.getFirstname());

    user.setLastname("Doe");
    assertEquals("Doe", user.getLastname());

    user.setMail("john.doe@example.com");
    assertEquals("john.doe@example.com", user.getMail());

    user.setBirthdate(LocalDate.of(1990, 1, 1));
    assertEquals(LocalDate.of(1990, 1, 1), user.getBirthdate());

    user.setFirebaseId("firebase123");
    assertEquals("firebase123", user.getFirebaseId());

    user.setRole(Role.CLIENT);
    assertEquals(Role.CLIENT, user.getRole());

    user.setSex(Sex.F);
    assertEquals(Sex.F, user.getSex());

    user.setCreationDatetime(Instant.now());
    assertNotNull(user.getCreationDatetime()); // Check if the creation datetime is not null

    user.setLastUpdateDatetime(Instant.now());
    assertNotNull(user.getLastUpdateDatetime()); // Check if the last update datetime is not null

    user.setPhotoKey("http://example.com/photo.jpg");
    assertEquals("http://example.com/photo.jpg", user.getPhotoKey());

    user.setBio("I am a blogger");
    assertEquals("I am a blogger", user.getBio());

    user.setProfileBannerKey("http://example.com/banner.jpg");
    assertEquals("http://example.com/banner.jpg", user.getProfileBannerKey());

    user.setUsername("john_doe");
    assertEquals("john_doe", user.getUsername());

    user.setAbout("About John Doe");
    assertEquals("About John Doe", user.getAbout());

    // Add tests for other fields and adjust values and assertions based on your business logic
  }

  @Test
  void testEqualsAndHashCode() {
    User user1 = new User();
    user1.setId("1");

    User user2 = new User();
    user2.setId("1");

    assertEquals(user1, user2);
    assertEquals(user1.hashCode(), user2.hashCode());
  }

  @Test
  void testBuilder() {
    // Use the builder to create a User instance with specific values
    User user =
        User.builder()
            .id("1")
            .firstname("John")
            .lastname("Doe")
            .mail("john.doe@example.com")
            .birthdate(LocalDate.of(1990, 1, 1))
            .firebaseId("firebase123")
            .role(Role.CLIENT)
            .sex(Sex.F)
            .creationDatetime(Instant.now())
            .lastUpdateDatetime(Instant.now())
            .photoKey("http://example.com/photo.jpg")
            .bio("I am a blogger")
            .profileBannerKey("http://example.com/banner.jpg")
            .username("john_doe")
            .about("About John Doe")
            .status(UserStatus.ENABLED)
            .build();

    // Check if the User instance has the expected values
    assertEquals("1", user.getId());
    assertEquals("John", user.getFirstname());
    assertEquals("Doe", user.getLastname());
    assertEquals("john.doe@example.com", user.getMail());
    assertEquals(LocalDate.of(1990, 1, 1), user.getBirthdate());
    assertEquals("firebase123", user.getFirebaseId());
    assertEquals(Role.CLIENT, user.getRole());
    assertEquals(Sex.F, user.getSex());
    assertNotNull(user.getCreationDatetime()); // Check if the creation datetime is not null
    assertNotNull(user.getLastUpdateDatetime()); // Check if the last update datetime is not null
    assertEquals("http://example.com/photo.jpg", user.getPhotoKey());
    assertEquals("I am a blogger", user.getBio());
    assertEquals("http://example.com/banner.jpg", user.getProfileBannerKey());
    assertEquals("john_doe", user.getUsername());
    assertEquals("About John Doe", user.getAbout());
    assertEquals(UserStatus.ENABLED, user.getStatus());

    // Add assertions for other fields if necessary
  }

  @Test
  public void testCrupdateUser() {
    User user = new User();
    user.setId("1");
    user.setFirstname("John");
    user.setLastname("Doe");
    user.setMail("john.doe@example.com");
    user.setBirthdate(LocalDate.of(1990, 1, 1));
    user.setFirebaseId("firebaseId");
    user.setRole(Role.CLIENT);
    user.setSex(Sex.M);
    user.setCreationDatetime(Instant.now());
    user.setLastUpdateDatetime(Instant.now());
    user.setPhotoKey("photoKey");
    user.setBio("Bio");
    user.setProfileBannerKey("bannerKey");
    user.setUsername("john_doe");
    user.setAbout("About");
    user.setStatus(UserStatus.ENABLED);
    user.setUserCategories(new ArrayList<>());
    user.setDeleted(false);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

    Mockito.doNothing().when(userValidator).accept(user);

    User updatedUser = userService.crupdateUser(user, user.getId());

    Mockito.verify(userRepository).findById(user.getId());

    Mockito.verify(userRepository).save(Mockito.any(User.class));

    assertEquals(user.getCreationDatetime(), updatedUser.getCreationDatetime());
    assertEquals(user.getRole(), updatedUser.getRole());
    assertEquals(user.getPhotoKey(), updatedUser.getPhotoKey());
    assertEquals(user.getProfileBannerKey(), updatedUser.getProfileBannerKey());
  }
}

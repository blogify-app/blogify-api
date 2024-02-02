package com.blogify.blogapi.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.model.enums.Role;
import com.blogify.blogapi.model.enums.Sex;
import com.blogify.blogapi.model.enums.UserStatus;
import com.blogify.blogapi.repository.model.User;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class UserUnitTest extends FacadeIT {

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

    user.setPhotoUrl("http://example.com/photo.jpg");
    assertEquals("http://example.com/photo.jpg", user.getPhotoUrl());

    user.setBio("I am a blogger");
    assertEquals("I am a blogger", user.getBio());

    user.setProfileBannerUrl("http://example.com/banner.jpg");
    assertEquals("http://example.com/banner.jpg", user.getProfileBannerUrl());

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
            .photoUrl("http://example.com/photo.jpg")
            .bio("I am a blogger")
            .profileBannerUrl("http://example.com/banner.jpg")
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
    assertEquals("http://example.com/photo.jpg", user.getPhotoUrl());
    assertEquals("I am a blogger", user.getBio());
    assertEquals("http://example.com/banner.jpg", user.getProfileBannerUrl());
    assertEquals("john_doe", user.getUsername());
    assertEquals("About John Doe", user.getAbout());
    assertEquals(UserStatus.ENABLED, user.getStatus());

    // Add assertions for other fields if necessary
  }
}

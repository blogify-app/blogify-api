package com.blogify.blogapi.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.enums.Role;
import com.blogify.blogapi.model.enums.Sex;
import com.blogify.blogapi.model.enums.UserStatus;
import com.blogify.blogapi.model.validator.UserValidator;
import com.blogify.blogapi.repository.PostRepository;
import com.blogify.blogapi.repository.UserRepository;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.UserService;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class UserUnitTest extends FacadeIT {

  @Mock private UserRepository userRepository;
  @Mock private PostRepository postRepository;

  @Mock private UserValidator userValidator;

  @InjectMocks private UserService userService;

  @Test
  void testGetterSetter() {
    User user = new User();

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
    when(userRepository.save(any(User.class))).thenReturn(user);

    Mockito.doNothing().when(userValidator).accept(user);

    User updatedUser = userService.crupdateUser(user, user.getId());

    verify(userRepository).findById(user.getId());

    verify(userRepository).save(any(User.class));

    assertEquals(user.getCreationDatetime(), updatedUser.getCreationDatetime());
    assertEquals(user.getRole(), updatedUser.getRole());
    assertEquals(user.getPhotoKey(), updatedUser.getPhotoKey());
    assertEquals(user.getProfileBannerKey(), updatedUser.getProfileBannerKey());
  }

  @Test
  public void testSaveSignUpUser() {
    User newUser = new User();
    newUser.setMail("test@example.com");

    when(userRepository.findByMail(eq("test@example.com"))).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(newUser);
    User savedUser = userService.saveSignUpUser(newUser);
    verify(userRepository).findByMail(eq("test@example.com"));
    verify(userRepository).save(eq(newUser));

    assertEquals(newUser, savedUser);
  }

  @Test
  public void testFindById() {
    String userId = "123";
    User existingUser = new User();
    existingUser.setId(userId);

    when(userRepository.findById(eq(userId))).thenReturn(Optional.of(existingUser));
    User foundUser = userService.findById(userId);
    verify(userRepository).findById(eq(userId));

    assertNotNull(foundUser);
    assertEquals(existingUser, foundUser);
  }

  @Test
  public void testFindAllByName() {
    User user1 =
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
    User user2 =
        User.builder()
            .id("2")
            .firstname("Henri")
            .lastname("HisLastname")
            .mail("henri@example.com")
            .birthdate(LocalDate.of(1985, 5, 15))
            .firebaseId("newFirebaseId")
            .role(Role.CLIENT)
            .sex(Sex.M)
            .creationDatetime(Instant.now())
            .lastUpdateDatetime(Instant.now())
            .photoKey("http://example.com/new_photo.jpg")
            .bio("Henri bio")
            .profileBannerKey("http://example.com/new_banner.jpg")
            .username("henri_username")
            .about("Henri about")
            .status(UserStatus.ENABLED)
            .build();

    PageFromOne page = new PageFromOne(1);
    BoundedPageSize pageSize = new BoundedPageSize(5);

    List<User> userList = new ArrayList<>();
    userList.add(user1);
    userList.add(user2);

    String name = "HisLastname";
    when(userRepository.getUserByName(eq(name), any(Pageable.class))).thenReturn(userList);
    List<User> result = userService.findAllByName(name, page, pageSize);
    verify(userRepository).getUserByName(eq(name), any(Pageable.class));

    assertEquals(List.of(userList.get(1)), List.of(result.get(1)));
    assertEquals("henri", result.get(1).getFirstname().toLowerCase());
  }

  @Test
  public void testCheckUserOfPost_UserMatches() {
    String userId = "1";
    String postId = "100";

    User user = new User();
    user.setId(userId);
    Post post = new Post();
    post.setId(postId);
    post.setUser(user);
    when(postRepository.findById(eq(postId))).thenReturn(Optional.of(post));

    boolean result = userService.checkUserOfPost(userId, postId);

    verify(postRepository).findById(eq(postId));

    assertTrue(result);
  }

  @Test
  public void testCheckUserOfPost_UserDoesNotMatch() {
    String userId = "1";
    String postId = "100";

    User user = new User();
    user.setId("2");
    Post post = new Post();
    post.setId(postId);
    post.setUser(user);
    when(postRepository.findById(eq(postId))).thenReturn(Optional.of(post));

    boolean result = userService.checkUserOfPost(userId, postId);

    verify(postRepository).findById(eq(postId));

    assertFalse(result);
  }

  @Test
  public void testCheckSelfUser_UserExistsAndMatches() {
    String userId = "1";
    String userIdFromEndpoint = "1";

    User user = new User();
    user.setId(userId);
    when(userRepository.findById(eq(userIdFromEndpoint))).thenReturn(Optional.of(user));

    boolean result = userService.checkSelfUser(userId, userIdFromEndpoint);

    verify(userRepository).findById(eq(userIdFromEndpoint));

    assertTrue(result);
  }

  @Test
  public void testCheckSelfUser_UserDoesNotExist() {
    String userId = "1";
    String userIdFromEndpoint = "2";

    when(userRepository.findById(eq(userIdFromEndpoint))).thenReturn(Optional.empty());

    boolean result = userService.checkSelfUser(userId, userIdFromEndpoint);

    verify(userRepository).findById(eq(userIdFromEndpoint));

    assertTrue(result);
  }

  @Test
  public void testCheckSelfUser_UserExistsButDoesNotMatch() {
    String userId = "1";
    String userIdFromEndpoint = "2";

    User user = new User();
    user.setId(userIdFromEndpoint);
    when(userRepository.findById(eq(userIdFromEndpoint))).thenReturn(Optional.of(user));

    boolean result = userService.checkSelfUser(userId, userIdFromEndpoint);

    verify(userRepository).findById(eq(userIdFromEndpoint));

    assertFalse(result);
  }
}

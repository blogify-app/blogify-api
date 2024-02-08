package com.blogify.blogapi.unit.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.model.validator.UserValidator;
import com.blogify.blogapi.repository.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class UserValidatorUnitTest {

  @InjectMocks
  private UserValidator userValidator;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testValidation_Success() {
    User user = new User();
    user.setId("1");
    user.setFirstname("John");
    user.setLastname("Doe");
    user.setMail("john.doe@example.com");
    user.setBirthdate(LocalDate.of(1990, 1, 1));

    assertDoesNotThrow(() -> userValidator.accept(user));
  }

  @Test
  public void testValidation_Failure() {
    User user = new User();
    user.setFirstname("John");
    user.setMail("John.gmail.com");

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> userValidator.accept(user));

    assertTrue(exception.getMessage().contains("Last name is mandatory"));
    assertTrue(exception.getMessage().contains("Email format is invalid"));
    assertTrue(exception.getMessage().contains("User ID is mandatory"));
    assertTrue(exception.getMessage().contains("Birthdate is mandatory"));
  }

  @Test
  public void testAccept_Success() {
    List<User> users = new ArrayList<>();
    User user1 = new User();
    user1.setId("1");
    user1.setFirstname("John");
    user1.setLastname("Doe");
    user1.setMail("john.doe@example.com");
    user1.setBirthdate(LocalDate.of(1990, 1, 1));
    users.add(user1);

    User user2 = new User();
    user2.setId("2");
    user2.setFirstname("Jane");
    user2.setLastname("Doe");
    user2.setMail("jane.doe@example.com");
    user2.setBirthdate(LocalDate.of(1990, 2, 1));
    users.add(user2);

    assertDoesNotThrow(() -> userValidator.accept(users));
  }

  @Test
  public void testAccept_Failure() {
    List<User> users = new ArrayList<>();
    User user1 = new User();
    user1.setId("1");
    user1.setFirstname("John");

    users.add(user1);

    assertThrows(BadRequestException.class, () -> userValidator.accept(users));
  }
}

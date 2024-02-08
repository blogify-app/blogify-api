package com.blogify.blogapi.unit.validator;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.model.enums.CommentStatus;
import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.model.validator.CommentValidator;
import com.blogify.blogapi.repository.model.Comment;
import com.blogify.blogapi.repository.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentValidatorUnitTest extends FacadeIT {
  private CommentValidator commentValidator;

  @BeforeEach
  void setUp() {
    commentValidator = new CommentValidator();
  }

  @Test
  void testAccept_WithValidComment() {
    Comment comment = new Comment();
    comment.setContent("This is a valid comment content.");
    User user = new User();
    user.setId("userId");
    comment.setUser(user);

    assertDoesNotThrow(() -> commentValidator.accept(comment));
  }

  @Test
  void testAccept_WithNullContent() {
    Comment comment = new Comment();
    User user = new User();
    user.setId("userId");
    comment.setUser(user);

    assertThrows(BadRequestException.class, () -> commentValidator.accept(comment));
  }

  @Test
  void testAccept_WithNullUser() {
    Comment comment = new Comment();
    comment.setContent("This is a valid comment content.");

    assertThrows(BadRequestException.class, () -> commentValidator.accept(comment));
  }

  @Test
  void testAccept_WithInvalidCommentStatus() {
    Comment comment = new Comment();
    comment.setContent("This is a valid comment content.");
    User user = new User();
    user.setId("userId");
    comment.setUser(user);
    comment.setStatus(CommentStatus.DISABLED);

    assertThrows(BadRequestException.class, () -> commentValidator.accept(comment));
  }
}

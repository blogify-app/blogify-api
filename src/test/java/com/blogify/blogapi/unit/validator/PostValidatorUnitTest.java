package com.blogify.blogapi.unit.validator;

import static org.junit.jupiter.api.Assertions.*;

import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.model.validator.PostValidator;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class PostValidatorUnitTest {

  @InjectMocks private PostValidator postValidator;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testAcceptWithValidPost() {
    Post post = createValidPost();

    assertDoesNotThrow(() -> postValidator.accept(post));
  }

  @Test
  public void testAcceptWithInvalidPost() {
    Post post = createValidPost();
    post.setId(null);

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> postValidator.accept(post));
    assertEquals("Post_id is mandatory", exception.getMessage());
  }

  @Test
  public void testAcceptWithInvalidUserId() {
    Post post = createValidPost();
    post.setUser(null);

    BadRequestException exception =
        assertThrows(BadRequestException.class, () -> postValidator.accept(post));
    assertEquals("User is mandatory", exception.getMessage());
  }

  private Post createValidPost() {
    Post post = new Post();
    post.setId("1");
    post.setUser(new User());
    return post;
  }
}

package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.COMMENT2_ID;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment1;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment2;
import static com.blogify.blogapi.integration.conf.MockData.CommentMockData.comment3;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST2_ID;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.CommentsApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.integration.conf.AbstractContextInitializer;
import com.blogify.blogapi.integration.conf.TestUtils;
import com.blogify.blogapi.service.firebase.FirebaseService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestComponent
@ContextConfiguration(initializers = CommentIT.ContextInitializer.class)
public class CommentIT {
  @MockBean private FirebaseService firebaseServiceMock;

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, CommentIT.ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
  }

  @Test
  void client_read_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    CommentsApi api = new CommentsApi(client1Client);

    Comment commentByPostId = api.getCommentById(POST1_ID,COMMENT2_ID);
    List<Comment> allPost1Comments = api.getCommentsByPostId(POST1_ID, 1, 10);

    List<Comment> allPost2Comments = api.getCommentsByPostId(POST2_ID, 1, 10);

    assertEquals("this in the content2",commentByPostId.getContent());
    assertEquals(3, allPost1Comments.size());
    assertTrue(allPost1Comments.contains(comment1()));
    assertTrue(allPost1Comments.contains(comment2()));
    assertTrue(allPost1Comments.contains(comment3()));

    assertEquals(0, allPost2Comments.size());
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

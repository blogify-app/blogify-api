package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST2_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post1;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post2;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.getDislikeReactionPoint;
import static com.blogify.blogapi.integration.conf.TestUtils.getLikeReactionPoint;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.PostingApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.Reaction;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.integration.conf.AbstractContextInitializer;
import com.blogify.blogapi.integration.conf.TestUtils;
import com.blogify.blogapi.service.firebase.FirebaseService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestComponent
@ContextConfiguration(initializers = ReactionIt.ContextInitializer.class)
public class ReactionIt {
  @MockBean private FirebaseService firebaseServiceMock;

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, ReactionIt.ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
  }

  @Test
  void client_write_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    ApiClient client2Client = apiClient(CLIENT2_TOKEN);
    PostingApi api2 = new PostingApi(client2Client);

    List<Post> allPostsBefore = api.getPosts(1, 10, null);

    Reaction post1DislikeByClient1 = api.reactToPostById(POST1_ID, ReactionType.DISLIKE);

    List<Post> allPosts1 = api.getPosts(1, 10, null);

    Reaction post2likeByClient2 = api2.reactToPostById(POST2_ID, ReactionType.LIKE);

    List<Post> allPosts2 = api.getPosts(1, 10, null);

    assertEquals(2, allPostsBefore.size());
    assertTrue(allPostsBefore.contains(post1()));
    assertTrue(allPostsBefore.contains(post2()));
    assertEquals(BigDecimal.valueOf(2), getLikeReactionPoint(allPostsBefore, POST1_ID));
    assertEquals(BigDecimal.valueOf(0), getDislikeReactionPoint(allPostsBefore, POST1_ID));
    assertEquals(BigDecimal.valueOf(0), getLikeReactionPoint(allPostsBefore, POST2_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPostsBefore, POST2_ID));

    assertEquals(2, allPosts1.size());
    assertEquals(BigDecimal.valueOf(1), getLikeReactionPoint(allPosts1, POST1_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPosts1, POST1_ID));
    assertEquals(BigDecimal.valueOf(0), getLikeReactionPoint(allPosts1, POST2_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPosts1, POST2_ID));

    assertEquals(2, allPosts2.size());
    assertEquals(BigDecimal.valueOf(1), getLikeReactionPoint(allPosts2, POST1_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPosts2, POST1_ID));
    assertEquals(BigDecimal.valueOf(1), getLikeReactionPoint(allPosts2, POST2_ID));
    assertEquals(BigDecimal.valueOf(1), getDislikeReactionPoint(allPosts2, POST2_ID));
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

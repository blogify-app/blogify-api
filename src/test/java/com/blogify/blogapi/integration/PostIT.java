package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY1_LABEL;
import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY2_LABEL;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.CREATE_POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post1;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post2;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.postToCreate;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.PostingApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Post;
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
@ContextConfiguration(initializers = PostIT.ContextInitializer.class)
public class PostIT {
  @MockBean private FirebaseService firebaseServiceMock;

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, PostIT.ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
  }

  @Test
  void client_read_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    List<Post> allPosts = api.getPosts(1, 10, null);

    List<Post> allPostsWithCategory1Or2 =
        api.getPosts(1, 10, CATEGORY2_LABEL + "," + CATEGORY1_LABEL);
    List<Post> allPostsWithCategory2 = api.getPosts(1, 10, CATEGORY2_LABEL);

    assertEquals(2, allPosts.size());
    assertTrue(allPosts.contains(post1()));
    assertTrue(allPosts.contains(post2()));

    assertEquals(2, allPostsWithCategory1Or2.size());
    assertTrue(allPostsWithCategory1Or2.contains(post1()));
    assertTrue(allPostsWithCategory1Or2.contains(post2()));

    assertEquals(1, allPostsWithCategory2.size());
    assertTrue(allPostsWithCategory2.contains(post1()));
  }

  @Test
  void client_write_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    String newContent = "new content";

    List<Post> allPostsBeforeUpdate = api.getPosts(1, 10, null);

    Post updatePost1 = api.crupdatePostById(POST1_ID, post1().content(newContent));

    List<Post> allPostsAfterUpdate = api.getPosts(1, 10, null);

    Post createdPost1 = api.crupdatePostById(CREATE_POST1_ID, postToCreate());

    List<Post> allPostsAfterCreate = api.getPosts(1, 10, null);

    assertEquals(2, allPostsBeforeUpdate.size());
    assertTrue(allPostsBeforeUpdate.contains(post1()));
    assertTrue(allPostsBeforeUpdate.contains(post2()));

    assertEquals(post1().content(newContent).updatedAt(updatePost1.getUpdatedAt()), updatePost1);

    assertEquals(2, allPostsAfterUpdate.size());
    assertFalse(allPostsAfterUpdate.contains(post1()));
    assertFalse(allPostsAfterUpdate.contains(post1().content(newContent)));
    assertTrue(allPostsAfterUpdate.contains(post2()));

    assertEquals(CREATE_POST1_ID, createdPost1.getId());

    assertEquals(3, allPostsAfterCreate.size());
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

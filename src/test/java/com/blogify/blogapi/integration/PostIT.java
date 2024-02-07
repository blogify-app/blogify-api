package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY1_LABEL;
import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY2_LABEL;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.CREATE_POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post1;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post2;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.postToCreate;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client1;
import static com.blogify.blogapi.integration.conf.TestUtils.BAD_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.assertThrowsApiException;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpS3Service;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.PostingApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.integration.conf.AbstractContextInitializer;
import com.blogify.blogapi.integration.conf.TestUtils;
import com.blogify.blogapi.service.firebase.FirebaseService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestComponent
@ContextConfiguration(initializers = PostIT.ContextInitializer.class)
public class PostIT {
  @MockBean private FirebaseService firebaseServiceMock;
  @MockBean private S3Service s3Service;

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, PostIT.ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
    setUpS3Service(s3Service);
  }

  @Test
  void client_read_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    Post actualPost = api.getPostById(POST1_ID);
    List<Post> allPosts = api.getPosts(1, 10, null);

    List<Post> allPostsWithCategory1Or2 =
        api.getPosts(1, 10, CATEGORY2_LABEL + "," + CATEGORY1_LABEL);
    List<Post> allPostsWithCategory2 = api.getPosts(1, 10, CATEGORY2_LABEL);

    assertEquals(post1(), actualPost);
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
  void read_post_by_id_ko() {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client1Client);
    String postId = randomUUID().toString();

    assertThrowsApiException(
        "{\"type\":\"404 NOT_FOUND\",\"message\":\"Resource of type Post identified by "
            + postId
            + " not found\"}",
        () -> api.getPostById(postId));
  }

  @Test
  void read_posts_by_user_id() throws ApiException {
    ApiClient client = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client);

    List<Post> actuals = api.getPostsByUserId(client1().getId(), 1, 20);

    assertEquals(1, actuals.size());
  }

  @Test
  @DirtiesContext
  void client_delete_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    List<Post> allPosts = api.getPosts(1, 10, null);

    api.deletePostById(POST1_ID);

    List<Post> allPostsAfterDelete = api.getPosts(1, 10, null);

    assertEquals(2, allPosts.size());
    assertTrue(allPosts.contains(post1()));
    assertTrue(allPosts.contains(post2()));

    assertEquals(1, allPostsAfterDelete.size());
    assertFalse(allPostsAfterDelete.contains(post1()));
    assertTrue(allPostsAfterDelete.contains(post2()));
  }

  @Test
  @DirtiesContext
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

  @Test
  @DirtiesContext
  void client_update_ko() throws ApiException {
    ApiClient client1 = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client1);
    Post postUpdate1 = api.getPostById(POST1_ID).id(null);
    Post postUpdate2 = api.getPostById(POST1_ID).author(null);

    ApiException exception1 =
        assertThrows(ApiException.class, () -> api.crupdatePostById(POST1_ID, postUpdate1));
    ApiException exception2 =
        assertThrows(ApiException.class, () -> api.crupdatePostById(POST1_ID, postUpdate2));

    String exceptionMessage1 = exception1.getMessage();
    String exceptionMessage2 = exception2.getMessage();

    assertTrue(exceptionMessage1.contains("Post_id is mandatory"));
    assertTrue(exceptionMessage2.contains("Author_id is mandatory"));
  }

  @Test
  void not_authenticate_create_ko() {
    ApiClient client1Client = apiClient(BAD_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    assertThrowsApiException(
        "{\"type\":\"403 FORBIDDEN\",\"message\":\"Bearer token is expired or invalid\"}",
        () -> api.crupdatePostById(CREATE_POST1_ID, postToCreate()));
  }

  @Test
  void not_authenticate_update_ko() {
    ApiClient client1Client = apiClient(BAD_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    assertThrowsApiException(
        "{\"type\":\"403 FORBIDDEN\",\"message\":\"Bearer token is expired or invalid\"}",
        () -> api.crupdatePostById(POST1_ID, post1().content("new content")));
  }

  @Test
  void other_client_update_ko() {
    ApiClient client2Client = apiClient(CLIENT2_TOKEN);
    PostingApi api = new PostingApi(client2Client);

    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> api.crupdatePostById(POST1_ID, post1().content("new content")));

    assertTrue(exception.getMessage().contains("status\":403,\"error\":\"Forbidden"));
  }

  @Test
  void other_client_delete_ko(){
    ApiClient client2 = apiClient(CLIENT2_TOKEN);
    PostingApi api = new PostingApi(client2);

    ApiException exception =
            assertThrows(
                    ApiException.class,
                    () -> api.deletePostById(POST1_ID)
            );
    assertTrue(exception.getMessage().contains("status\":403,\"error\":\"Forbidden"));
  }

  @Test
  void not_authenticate_create_reaction_ko(){
    ApiClient client = apiClient(BAD_TOKEN);
    PostingApi api = new PostingApi(client);

    assertThrowsApiException(
            "{\"type\":\"403 FORBIDDEN\",\"message\":\"Bearer token is expired or invalid\"}",
            () -> api.reactToPostById(POST1_ID,ReactionType.LIKE)
    );
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

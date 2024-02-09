package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY1_LABEL;
import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.CATEGORY2_LABEL;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST2_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST3_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post1;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post2;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.post3;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.CLIENT1_ID;
import static com.blogify.blogapi.integration.conf.TestUtils.BAD_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.assertThrowsApiException;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpS3Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.PostingApi;
import com.blogify.blogapi.endpoint.rest.api.UserApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Post;
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
@ContextConfiguration(initializers = AIIT.ContextInitializer.class)
public class AIIT {
  @MockBean private FirebaseService firebaseServiceMock;
  @MockBean private S3Service s3Service;

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, AIIT.ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
    setUpS3Service(s3Service);
  }

  @Test
  void not_authenticate_can_have_suggestion_ko() {
    ApiClient client1Client = apiClient(BAD_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    assertThrowsApiException(
        "{\"type\":\"403 FORBIDDEN\",\"message\":\"Bearer token is expired or invalid\"}",
        () -> api.getSuggestedPosts(1, 10, null));
  }

  @Test
  void client_can_have_suggestion_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    PostingApi api = new PostingApi(client1Client);

    List<Post> allPosts = api.getSuggestedPosts(1, 10, null);

    List<Post> allPostsWithCategory1Or2 =
        api.getSuggestedPosts(1, 10, CATEGORY2_LABEL + "," + CATEGORY1_LABEL);
    List<Post> allPostsWithCategory2 = api.getSuggestedPosts(1, 10, CATEGORY2_LABEL);

    assertEquals(3, allPosts.size());
    assertTrue(allPosts.contains(post1()));
    assertTrue(allPosts.contains(post2()));

    assertEquals(3, allPostsWithCategory1Or2.size());
    assertTrue(allPostsWithCategory1Or2.contains(post1()));
    assertTrue(allPostsWithCategory1Or2.contains(post2()));

    assertEquals(1, allPostsWithCategory2.size());
    assertTrue(allPostsWithCategory2.contains(post1()));
  }

  @DirtiesContext
  @Test
  void suggestion_change_from_post_othority_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    ApiClient client2Client = apiClient(CLIENT2_TOKEN);

    PostingApi api = new PostingApi(client1Client);
    PostingApi api2 = new PostingApi(client2Client);

    UserApi userApi = new UserApi(client1Client);

    List<Post> allPosts = api.getSuggestedPosts(1, 10, null);
    assertEquals(3, allPosts.size());

    // post1 is first because it hase cat1 and cat2, but others have just cat1
    assertEquals(post1(), allPosts.get(0));

    for (int i = 0; i < 5; i++) {
      userApi.userViewPost(CLIENT1_ID, POST2_ID);
    }
    List<Post> actual2 = api.getSuggestedPosts(1, 10, null);
    List<String> idsOfActual2 = actual2.stream().map(Post::getId).toList();
    assertEquals(3, actual2.size());

    // post2 is before post3 because ti hase more view
    assertTrue(idsOfActual2.indexOf(post2().getId()) < idsOfActual2.indexOf(post3().getId()));

    for (int i = 0; i < 10; i++) {
      userApi.userViewPost(CLIENT1_ID, POST3_ID);
    }
    List<Post> actual3 = api.getSuggestedPosts(1, 10, null);
    List<String> idsOfActual3 = actual3.stream().map(Post::getId).toList();
    assertEquals(3, actual3.size());

    // post3 is before post2 because ti hase more view
    assertTrue(idsOfActual3.indexOf(post3().getId()) < idsOfActual3.indexOf(post2().getId()));

    // post2 and post3 are before post1 because ti hase more view. and 5 view better than have more
    // cads
    assertTrue(idsOfActual3.indexOf(post3().getId()) < idsOfActual3.indexOf(post2().getId()));
    assertTrue(idsOfActual3.indexOf(post3().getId()) < idsOfActual3.indexOf(post1().getId()));
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

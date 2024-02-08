package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.PostMockData.PICTURE1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.PICTURE2_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.POST1_ID;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.postPicture1;
import static com.blogify.blogapi.integration.conf.MockData.PostMockData.postPicture2;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpS3Service;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.PostingApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.PostPicture;
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
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestComponent
@ContextConfiguration(initializers = PostIFileT.ContextInitializer.class)
public class PostIFileT {
  @MockBean private FirebaseService firebaseServiceMock;
  @MockBean private S3Service s3Service;

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, PostIFileT.ContextInitializer.SERVER_PORT);
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

    PostPicture actualPostPicture1 = api.getPostPictureById(POST1_ID, PICTURE1_ID);
    PostPicture actualPostPicture2 = api.getPostPictureById(POST1_ID, PICTURE2_ID);

    List<PostPicture> actualPostPictures = api.getAllPostPictureById(POST1_ID);

    assertEquals(postPicture1(), actualPostPicture1);
    assertEquals(postPicture2(), actualPostPicture2);

    assertEquals(2, actualPostPictures.size());
    assertTrue(actualPostPictures.contains(postPicture1()));
    assertTrue(actualPostPictures.contains(postPicture2()));
  }

  @Test
  void other_client_create_picture_ko() {
    ApiClient client = apiClient(CLIENT2_TOKEN);
    PostingApi api = new PostingApi((client));

    ApiException exception =
        assertThrows(ApiException.class, () -> api.uploadPostPicture(POST1_ID, PICTURE1_ID, null));

    assertTrue(exception.getMessage().contains("status\":403,\"error\":\"Forbidden"));
  }

  @Test
  void other_client_delete_picture_ko() {
    ApiClient client = apiClient(CLIENT2_TOKEN);
    PostingApi api = new PostingApi((client));

    ApiException exception =
        assertThrows(ApiException.class, () -> api.deletePostPictureById(POST1_ID, PICTURE1_ID));

    assertTrue(exception.getMessage().contains("status\":403,\"error\":\"Forbidden"));
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

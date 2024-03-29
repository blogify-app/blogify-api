package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.UserMockData.CLIENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.userPictureClient1Banner;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.userPictureClient1Profile;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpS3Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.UserApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.UserPicture;
import com.blogify.blogapi.endpoint.rest.model.UserPictureType;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.integration.conf.AbstractContextInitializer;
import com.blogify.blogapi.integration.conf.TestUtils;
import com.blogify.blogapi.service.firebase.FirebaseService;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestComponent
@ContextConfiguration(initializers = UserFileIT.ContextInitializer.class)
public class UserFileIT {

  @MockBean private FirebaseService firebaseServiceMock;
  @MockBean private S3Service s3Service;

  private static ApiClient anApiClient(String token) {
    return TestUtils.anApiClient(token, ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
    setUpS3Service(s3Service);
  }

  @Test
  void client_read_ok() throws ApiException {
    ApiClient client1Client = anApiClient(CLIENT1_TOKEN);
    UserApi api = new UserApi(client1Client);

    UserPicture actualUserPictureBanner = api.getUserPicture(CLIENT1_ID, UserPictureType.BANNER);
    UserPicture actualUserPictureProfile = api.getUserPicture(CLIENT1_ID, UserPictureType.PROFILE);

    assertEquals(actualUserPictureBanner, userPictureClient1Banner());
    assertEquals(actualUserPictureProfile, userPictureClient1Profile());
  }

  @Test
  void other_client_update_picture_ko() throws ApiException {
    ApiClient client2Client = anApiClient(CLIENT2_TOKEN);
    UserApi api = new UserApi(client2Client);

    ApiException exception1 =
        assertThrows(
            ApiException.class,
            () ->
                api.putUserPicture(
                    CLIENT1_ID, UserPictureType.PROFILE, new File("/home/test.jpg")));
    ApiException exception2 =
        assertThrows(
            ApiException.class,
            () ->
                api.putUserPicture(CLIENT1_ID, UserPictureType.BANNER, new File("/home/test.jpg")));
    assertTrue(exception1.getMessage().contains("status\":403,\"error\":\"Forbidden"));
    assertTrue(exception2.getMessage().contains("status\":403,\"error\":\"Forbidden"));
  }

  @Test
  void other_client_delete_picture_ko() throws ApiException {
    ApiClient client2Client = anApiClient(CLIENT2_TOKEN);
    UserApi api = new UserApi(client2Client);

    ApiException exception1 =
        assertThrows(
            ApiException.class, () -> api.deleteUserPicture(CLIENT1_ID, UserPictureType.PROFILE));
    ApiException exception2 =
        assertThrows(
            ApiException.class, () -> api.deleteUserPicture(CLIENT1_ID, UserPictureType.BANNER));

    assertTrue(exception1.getMessage().contains("status\":403,\"error\":\"Forbidden"));
    assertTrue(exception2.getMessage().contains("status\":403,\"error\":\"Forbidden"));
  }

  //  @Test
  //  void client_write_ok() throws ApiException {
  //    ApiClient client1Client = anApiClient(CLIENT1_TOKEN);
  //    UserApi api = new UserApi(client1Client);
  //
  //    UserPicture actual = api.putUserPicture(CLIENT1_ID, UserPictureType.PROFILE,
  // jpgMultipartFile());
  //
  //    assertEquals(userPictureClient1Profile().url(CLIENT1_PROFILE_URL_JPG), actual);
  //  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

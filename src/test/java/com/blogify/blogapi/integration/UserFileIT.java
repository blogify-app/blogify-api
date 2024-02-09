package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.UserMockData.CLIENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client1;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client2;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.userPictureClient1Banner;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.userPictureClient1Profile;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT2_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.assertThrowsApiException;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpS3Service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import com.blogify.blogapi.endpoint.rest.api.UserApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Category;
import com.blogify.blogapi.endpoint.rest.model.User;
import com.blogify.blogapi.endpoint.rest.model.UserPicture;
import com.blogify.blogapi.endpoint.rest.model.UserPictureType;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.integration.conf.AbstractContextInitializer;
import com.blogify.blogapi.integration.conf.MockData.CategoriesMockData;
import com.blogify.blogapi.integration.conf.TestUtils;
import com.blogify.blogapi.service.firebase.FirebaseService;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

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

//  @Test
//  void upload_file_ok() throws IOException, InterruptedException, ApiException {
//    Resource jpegFile = new ClassPathResource("files/upload.jpg");
//    Resource fakeExeFile = new ClassPathResource("files/jpeg-with-exe-extension.exe");
//    Resource pngFile = new ClassPathResource("files/png-file.png");
//
//    HttpResponse<byte[]> jpegResponse =
//        upload(UserPictureType.PROFILE, CLIENT1_ID, jpegFile.getFile());
//    HttpResponse<byte[]> fakeExeResponse =
//        upload(UserPictureType.PROFILE, CLIENT1_ID, fakeExeFile.getFile());
//    HttpResponse<byte[]> pngResponse =
//        upload(UserPictureType.PROFILE, CLIENT1_ID, pngFile.getFile());
//    assertEquals(HttpStatus.OK.value(), jpegResponse.statusCode());
//    assertEquals(HttpStatus.OK.value(), fakeExeResponse.statusCode());
//    assertEquals(HttpStatus.OK.value(), pngResponse.statusCode());
//    assertEquals(jpegFile.getInputStream().readAllBytes().length, jpegResponse.body().length);
////    assertEquals(MediaType.IMAGE_JPEG_VALUE, typeGuesser.detect(jpegResponse.body()));
//    assertEquals(fakeExeFile.getInputStream().readAllBytes().length, fakeExeResponse.body().length);
////    assertEquals(MediaType.IMAGE_JPEG_VALUE, typeGuesser.detect(fakeExeResponse.body()));
//    assertEquals(pngFile.getInputStream().readAllBytes().length, pngResponse.body().length);
//    // /!\ it seems nor the file is a fake png nor the guessed mediaType is always jpeg
//    // assertEquals(MediaType.IMAGE_PNG_VALUE, typeGuesser.detect(pngResponse.body()));
//    /* /!\ The file seems to get more bytes than initial with S3 localstack container
//    assertEquals(jpegFile.getInputStream().readAllBytes().length, downloadResponse.body().length);*/
//  }
//
//  @Test
//  void upload_file_ko() {
//    Resource toUpload = new ClassPathResource("files/real-exe-file.exe");
//
//    assertThrowsApiException(
//        "{\"type\":\"400 BAD_REQUEST\","
//            + "\"message\":\"Only png, jpeg/jpg files are allowed."
//            + "\"}",
//        () -> upload(UserPictureType.PROFILE, CLIENT1_ID, toUpload.getFile()));
//    assertThrowsApiException(
//        "{\"type\":\"400 BAD_REQUEST\","
//            + "\"message\":\"No enum constant app.bpartners.api.endpoint.rest"
//            + ".model.FileType.invalid_logo_type"
//            + "\"}",
//        () -> upload(UserPictureType.PROFILE, CLIENT1_ID, toUpload.getFile()));
//  }

  @Test
  void http_response_ok() throws IOException, InterruptedException, ApiException{
      HttpResponse<User> httpResponse = upload();

    System.out.println("+++++++++++++++++++++++++++++++++++++");
    System.out.println("+++++++++++++++++++++++++++++++++++++");
    System.out.println(httpResponse.body());
    System.out.println("+++++++++++++++++++++++++++++++++++++");
    System.out.println("+++++++++++++++++++++++++++++++++++++");

    assertEquals(HttpStatus.OK.value(),httpResponse.statusCode());
  }
  private HttpResponse<User> upload()
      throws IOException, InterruptedException, ApiException {
    HttpClient unauthenticatedClient = HttpClient.newBuilder().build();
    String basePath = "http://localhost:" + ContextInitializer.SERVER_PORT;

    HttpResponse<String> response =
        unauthenticatedClient.send(
            HttpRequest.newBuilder()
                .uri(
                    URI.create(
                        basePath
                            + "/users/"
                        + CLIENT1_ID
                            ))
                .header("Authorization", "Bearer " + CLIENT1_TOKEN)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build(), HttpResponse.BodyHandlers.ofString());
    ObjectMapper objectMapper = new ObjectMapper();
    User user = objectMapper.readValue(response.body(), User.class);
    return (HttpResponse<User>) user;
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

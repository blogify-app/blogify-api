package com.blogify.blogapi.integration.conf;

import static com.blogify.blogapi.integration.conf.MockData.FileMockData.jpgFileContent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.blogify.blogapi.constant.FileConstant;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.service.firebase.FirebaseService;
import com.blogify.blogapi.service.firebase.FirebaseUser;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.function.Executable;

public class TestUtils {

  public static final String BAD_TOKEN = "bad_token";
  public static final String CLIENT1_TOKEN = "client1_token";
  public static final String CLIENT2_TOKEN = "client2_token";
  public static final String MANAGER_TOKEN = "manger1_token";

  public static final String CLIENT1_PROFILE_KEY = "client1_profile_key";
  public static final String CLIENT1_BANNER_KEY = "client1_banner_key";
  public static final String CLIENT2_PROFILE_KEY = "client2_profile_key";
  public static final String CLIENT2_BANNER_KEY = "client2_banner_key";

  public static final String CLIENT1_PROFILE_URL = "https://profile.url.com/client1";
  public static final String CLIENT1_BANNER_URL = "https://banner.url.com/client1";
  public static final String CLIENT2_PROFILE_URL = "https://profile.url.com/client2";
  public static final String CLIENT2_BANNER_URL = "https://banner.url.com/client2";

  public static final String CLIENT1_PROFILE_URL_JPG = "https://profile.url.com/client1/jpg";

  public static final String POST1_PICTURE1_KEY = "post1_bucket_picture1_id_key";
  public static final String POST1_PICTURE2_KEY = "post1_bucket_picture2_id_key";
  public static final String POST1_PICTURE1_URL = "https://post1.url.com/picture1";
  public static final String POST1_PICTURE2_URL = "https://post1.url.com/picture2";

  public static final FirebaseUser firebaseUserClient1 =
      new FirebaseUser("test@gmail.com", "uQp7l4pzKuaaqCXjruhZw525pI23");
  public static final FirebaseUser firebaseUserClient2 =
      new FirebaseUser("hei.hajatiana@gmail.com", "W2O94puRphSI6HkaCP7kAjA9GFB2");
  public static final FirebaseUser firebaseUserManager1 =
      new FirebaseUser("test+vano@hei.school", "manager1_firebase_id");

  public static void setUpFirebase(FirebaseService firebaseService) {
    when(firebaseService.getUserByBearer(BAD_TOKEN)).thenReturn(null);
    when(firebaseService.getUserByBearer(CLIENT1_TOKEN)).thenReturn(firebaseUserClient1);
    when(firebaseService.getUserByBearer(CLIENT2_TOKEN)).thenReturn(firebaseUserClient2);
    when(firebaseService.getUserByBearer(MANAGER_TOKEN)).thenReturn(firebaseUserManager1);
  }

  public static void setUpS3Service(S3Service s3Service) {
    try {
      when(s3Service.generatePresignedUrl(CLIENT1_PROFILE_KEY, FileConstant.URL_DURATION))
          .thenReturn(new URL(CLIENT1_PROFILE_URL));
      when(s3Service.generatePresignedUrl(CLIENT1_BANNER_KEY, FileConstant.URL_DURATION))
          .thenReturn(new URL(CLIENT1_BANNER_URL));
      when(s3Service.generatePresignedUrl(CLIENT2_PROFILE_KEY, FileConstant.URL_DURATION))
          .thenReturn(new URL(CLIENT2_PROFILE_URL));
      when(s3Service.generatePresignedUrl(CLIENT2_BANNER_KEY, FileConstant.URL_DURATION))
          .thenReturn(new URL(CLIENT2_BANNER_URL));
      when(s3Service.generatePresignedUrl(POST1_PICTURE1_KEY, FileConstant.URL_DURATION))
          .thenReturn(new URL(POST1_PICTURE1_URL));
      when(s3Service.generatePresignedUrl(POST1_PICTURE2_KEY, FileConstant.URL_DURATION))
          .thenReturn(new URL(POST1_PICTURE2_URL));

      when(s3Service.uploadObjectToS3Bucket(CLIENT1_PROFILE_KEY, jpgFileContent()))
          .thenReturn(CLIENT1_PROFILE_URL_JPG);

    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  public static BigDecimal getLikeReactionPoint(List<Post> posts, String postId) {
    return Objects.requireNonNull(
            posts.stream()
                .filter(post -> Objects.equals(post.getId(), postId))
                .findFirst()
                .get()
                .getReactions())
        .getLikes();
  }

  public static BigDecimal getDislikeReactionPoint(List<Post> posts, String postId) {
    return Objects.requireNonNull(
            posts.stream()
                .filter(post -> Objects.equals(post.getId(), postId))
                .findFirst()
                .get()
                .getReactions())
        .getDislikes();
  }

  public static BigDecimal getLikeReactionPointInComment(List<Comment> comments, String commentId) {
    return Objects.requireNonNull(
            comments.stream()
                .filter(comment -> Objects.equals(comment.getId(), commentId))
                .findFirst()
                .get()
                .getReactions())
        .getLikes();
  }

  public static BigDecimal getDislikeReactionPointInComment(
      List<Comment> comments, String commentId) {
    return Objects.requireNonNull(
            comments.stream()
                .filter(comment -> Objects.equals(comment.getId(), commentId))
                .findFirst()
                .get()
                .getReactions())
        .getDislikes();
  }

  public static ApiClient anApiClient(String token, int serverPort) {
    ApiClient client = new ApiClient();
    client.setScheme("http");
    client.setHost("localhost");
    client.setPort(serverPort);
    client.setRequestInterceptor(
        httpRequestBuilder -> httpRequestBuilder.header("Authorization", "Bearer " + token));
    return client;
  }

  public static void assertThrowsApiException(String expectedBody, Executable executable) {
    ApiException apiException = assertThrows(ApiException.class, executable);
    assertEquals(expectedBody, apiException.getResponseBody());
  }

  public static void assertThrowsBadRequestException(String expectedBody, Executable executable) {
    BadRequestException badRequestException = assertThrows(BadRequestException.class, executable);
    assertEquals(expectedBody, badRequestException.getMessage());
  }

  public static void assertThrowsForbiddenException(Executable executable) {
    ApiException apiException = assertThrows(ApiException.class, executable);
    String responseBody = apiException.getResponseBody();
    assertEquals(
        "{" + "\"type\":\"403 FORBIDDEN\"," + "\"message\":\"Access is denied\"}", responseBody);
  }

  public static int anAvailableRandomPort() {
    try {
      return new ServerSocket(0).getLocalPort();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

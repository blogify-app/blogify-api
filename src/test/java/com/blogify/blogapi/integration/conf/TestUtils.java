package com.blogify.blogapi.integration.conf;

import static org.mockito.Mockito.when;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.service.firebase.FirebaseService;
import com.blogify.blogapi.service.firebase.FirebaseUser;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.util.List;
import java.util.Objects;

public class TestUtils {

  public static final String BAD_TOKEN = "bad_token";
  public static final String CLIENT1_TOKEN = "client1_token";

  public static final String CLIENT2_TOKEN = "client2_token";
  public static final String CLIENT3_TOKEN = "client3_token";

  public static final String MANAGER_TOKEN = "manger1_token";

  public static final FirebaseUser firebaseUserClient1 =
      new FirebaseUser("test+ryan@hei.school", "client1_firebase_id");

  public static final FirebaseUser firebaseUserClient2 =
      new FirebaseUser("test+herilala@hei.school", "client2_firebase_id");
  public static final FirebaseUser firebaseUserClient3 =
      new FirebaseUser("test+herizo@hei.school", "client3_firebase_id");
  public static final FirebaseUser firebaseUserManager1 =
      new FirebaseUser("test+vano@hei.school", "manager1_firebase_id");

  public static void setUpFirebase(FirebaseService firebaseService) {
    when(firebaseService.getUserByBearer(BAD_TOKEN)).thenReturn(null);

    when(firebaseService.getUserByBearer(CLIENT1_TOKEN)).thenReturn(firebaseUserClient1);

    when(firebaseService.getUserByBearer(CLIENT2_TOKEN)).thenReturn(firebaseUserClient2);

    when(firebaseService.getUserByBearer(CLIENT3_TOKEN)).thenReturn(firebaseUserClient3);

    when(firebaseService.getUserByBearer(MANAGER_TOKEN)).thenReturn(firebaseUserManager1);
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

  public static int anAvailableRandomPort() {
    try {
      return new ServerSocket(0).getLocalPort();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

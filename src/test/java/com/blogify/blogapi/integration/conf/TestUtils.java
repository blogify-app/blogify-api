package com.blogify.blogapi.integration.conf;

import static org.mockito.Mockito.when;

import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.service.firebase.FirebaseService;
import com.blogify.blogapi.service.firebase.FirebaseUser;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;

public class TestUtils {

  public static final String BAD_TOKEN = "bad_token";
  public static final String CLIENT1_TOKEN = "client1_token";
  public static final String MANAGER_TOKEN = "manger1_token";

  public static final FirebaseUser firebaseUserClient1 =
      new FirebaseUser("test+ryan@hei.school", "client1_firebase_id");
  public static final FirebaseUser firebaseUserManager1 =
      new FirebaseUser("test+vano@hei.school", "manager1_firebase_id");

  public static void setUpFirebase(FirebaseService firebaseService) {
    when(firebaseService.getUserByBearer(BAD_TOKEN)).thenReturn(null);

    when(firebaseService.getUserByBearer(CLIENT1_TOKEN)).thenReturn(firebaseUserClient1);

    when(firebaseService.getUserByBearer(MANAGER_TOKEN)).thenReturn(firebaseUserManager1);
  }

  public static boolean isBefore(String a, String b) {
    return a.compareTo(b) < 0;
  }

  public static boolean isBefore(int a, int b) {
    return a < b;
  }

  public static boolean isValidUUID(String candidate) {
    try {
      UUID.fromString(candidate);
      return true;
    } catch (Exception e) {
      return false;
    }
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

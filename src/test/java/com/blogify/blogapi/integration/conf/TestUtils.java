package com.blogify.blogapi.integration.conf;

import static org.mockito.Mockito.when;

import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.service.firebase.FirebaseService;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;

public class TestUtils {

  public static final String BAD_TOKEN = "bad_token";
  public static final String CLIENT1_TOKEN = "client1_token";
  public static final String MANAGER_TOKEN = "manger1_token";

  public static void setUpFirebase(FirebaseService firebaseService) {
    when(firebaseService.getUserByBearer(BAD_TOKEN)).thenReturn(null);

    when(firebaseService.getUserByBearer(CLIENT1_TOKEN)).thenReturn(null);

    when(firebaseService.getUserByBearer(MANAGER_TOKEN)).thenReturn(null);
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

package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.UserMockData.CLIENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.CLIENT2_ID;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.CREATED_CLIENT_ID;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client1;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client2;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.clientToCreate;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.manager1;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.signUpToCreate;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.assertThrowsApiException;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpS3Service;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogify.blogapi.endpoint.rest.api.SecurityApi;
import com.blogify.blogapi.endpoint.rest.api.UserApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.User;
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
@ContextConfiguration(initializers = UserIT.ContextInitializer.class)
public class UserIT {

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

    User actualUser = api.getUserById(CLIENT1_ID);
    List<User> actual = api.getUsers(1, 5, null);
    List<User> usersWithFilterName1 = api.getUsers(1, 5, "username");
    List<User> usersWithFilterName2 = api.getUsers(1, 5, "heRiLala");
    assertEquals(client1(), actualUser);
    assertEquals(3, actual.size());
    assertTrue(actual.contains(client1()));
    assertTrue(actual.contains(client2()));
    assertTrue(actual.contains(manager1()));

    assertEquals(3, usersWithFilterName1.size());

    assertEquals(1, usersWithFilterName2.size());
    assertTrue(usersWithFilterName2.contains(client2()));
  }

  @Test
  void read_user_by_id_ok() throws ApiException {
    ApiClient client = anApiClient(CLIENT1_TOKEN);
    UserApi api = new UserApi(client);

    User actual = api.getUserById(client1().getId());

    assertEquals(client1(), actual);
  }

  @Test
  void read_user_by_id_ko() {
    ApiClient client = anApiClient(CLIENT1_TOKEN);
    UserApi api = new UserApi(client);
    String userId = randomUUID().toString();

    assertThrowsApiException(
        "{\"type\":\"404 NOT_FOUND\",\"message\":\"User with id " + userId + " not found\"}",
        () -> api.getUserById(userId));
  }

  @Test
  void client_write_ok() throws ApiException {
    ApiClient client1Client = anApiClient(CLIENT1_TOKEN);
    UserApi api = new UserApi(client1Client);
    SecurityApi securityApi = new SecurityApi(client1Client);

    String newName = "new last Name";

    User actualUser = api.getUserById(CLIENT1_ID);
    List<User> actual = api.getUsers(1, 10, null);

    User actualUpdated = api.crupdateUserById(CLIENT2_ID, client2().lastName(newName));
    List<User> actualAfterUpdate = api.getUsers(1, 10, null);

    User user = securityApi.signUp(signUpToCreate());
    User actualCreated = api.crupdateUserById(CREATED_CLIENT_ID, clientToCreate());
    List<User> actualAfterCreate = api.getUsers(1, 10, null);

    assertEquals(client1(), actualUser);
    assertEquals(3, actual.size());
    assertTrue(actual.contains(client1()));
    assertTrue(actual.contains(client2()));
    assertTrue(actual.contains(manager1()));

    assertEquals(newName, actualUpdated.getLastName());
    assertEquals(3, actualAfterUpdate.size());

    assertEquals(clientToCreate().getFirstName(), actualCreated.getFirstName());
    assertEquals(clientToCreate().getLastName(), actualCreated.getLastName());
    assertEquals(clientToCreate().getUsername(), actualCreated.getUsername());
    assertEquals(clientToCreate().getAbout(), actualCreated.getAbout());
    assertEquals(clientToCreate().getEmail(), actualCreated.getEmail());
    assertEquals(clientToCreate().getBirthDate(), actualCreated.getBirthDate());
    assertEquals(clientToCreate().getSex(), actualCreated.getSex());
    assertEquals(clientToCreate().getStatus(), actualCreated.getStatus());
    assertEquals(clientToCreate().getPhotoUrl(), actualCreated.getPhotoUrl());
    assertEquals(clientToCreate().getBio(), actualCreated.getBio());
    assertEquals(clientToCreate().getProfileBannerUrl(), actualCreated.getProfileBannerUrl());
    assertEquals(clientToCreate().getCategories(), actualCreated.getCategories());
    assertEquals(4, actualAfterCreate.size());
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

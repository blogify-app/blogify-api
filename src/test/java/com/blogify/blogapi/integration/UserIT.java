package com.blogify.blogapi.integration;

import static com.blogify.blogapi.integration.conf.MockData.UserMockData.CLIENT1_ID;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client1;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.client2;
import static com.blogify.blogapi.integration.conf.MockData.UserMockData.manager1;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import com.blogify.blogapi.endpoint.rest.api.UserApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.User;
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
@ContextConfiguration(initializers = UserIT.ContextInitializer.class)
public class UserIT {

  @MockBean
  private FirebaseService firebaseServiceMock;

  private static ApiClient anApiClient(String token) {
    return TestUtils.anApiClient(token, ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
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
  @DirtiesContext
  void client_write_ok() throws ApiException {
    ApiClient client1Client = anApiClient(CLIENT1_TOKEN);
    UserApi api = new UserApi(client1Client);

    List<User> usersBeforeCreate = api.getUsers(1, 5, null);
    User actualUser = api.crupdateUserById(CLIENT1_ID, client1());
    List<User> usersAfterCreate = api.getUsers(1, 5, null);

    assertEquals(actualUser.getId(), CLIENT1_ID);
    assertEquals(3, usersBeforeCreate.size());
    assertEquals(4, usersAfterCreate.size());
    assertFalse(usersBeforeCreate.contains(client1()));
    assertEquals(usersAfterCreate.get(3).getId(), CLIENT1_ID);
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

package com.blogify.blogapi.integration;

import com.blogify.blogapi.endpoint.rest.api.CategoryApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Category;
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

import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.category1;
import static com.blogify.blogapi.integration.conf.MockData.CategoriesMockData.category2;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestComponent
@ContextConfiguration(initializers = CategoryIT.ContextInitializer.class)
public class CategoryIT {
  @MockBean private FirebaseService firebaseServiceMock;

  private static ApiClient apiClient(String token) {
    return TestUtils.anApiClient(token, CategoryIT.ContextInitializer.SERVER_PORT);
  }

  @BeforeEach
  void setUp() {
    setUpFirebase(firebaseServiceMock);
  }

  @Test
  void client_read_ok() throws ApiException {
    ApiClient client1Client = apiClient(CLIENT1_TOKEN);
    CategoryApi api = new CategoryApi(client1Client);

    List<Category> actual = api.getCategories(null);

    List<Category> actualByCriteria = api.getCategories("math");

    assertEquals(2, actual.size());
    assertTrue(actual.contains(category1()));
    assertTrue(actual.contains(category2()));

    assertEquals(1, actualByCriteria.size());
    assertTrue(actualByCriteria.contains(category1()));
  }

  static class ContextInitializer extends AbstractContextInitializer {
    public static final int SERVER_PORT = anAvailableRandomPort();

    @Override
    public int getServerPort() {
      return SERVER_PORT;
    }
  }
}

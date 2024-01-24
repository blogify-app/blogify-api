package com.blogify.blogapi.integration;

import com.blogify.blogapi.endpoint.rest.api.PostingApi;
import com.blogify.blogapi.endpoint.rest.client.ApiClient;
import com.blogify.blogapi.endpoint.rest.client.ApiException;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.integration.conf.AbstractContextInitializer;
import com.blogify.blogapi.integration.conf.TestUtils;
import com.blogify.blogapi.service.firebase.FirebaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.blogify.blogapi.integration.conf.PostMockData.post1;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestComponent
@ContextConfiguration(initializers = PostIt.ContextInitializer.class)
public class PostIt {
    @MockBean
    private FirebaseService firebaseServiceMock;

    private static ApiClient apiClient(String token){
        return TestUtils.anApiClient(token, UserIt.ContextInitializer.SERVER_PORT);
    }
    @BeforeEach
    void setUp() {
        setUpFirebase(firebaseServiceMock);
    }

    @Test
    void client_read_ok() throws ApiException {
        ApiClient client1Client = apiClient(CLIENT1_TOKEN);
        PostingApi api = new PostingApi(client1Client);

        List<Post> actual = api.getPosts(1,5,null);

        assertTrue(actual.contains(post1()));

        /**
         * List<User> actual = api.getUsers(1, 5, null);
         *         List<User> usersWithFilterName1 = api.getUsers(1, 5, "username");
         *         List<User> usersWithFilterName2 = api.getUsers(1, 5, "heRiLala");
         *
         *         assertEquals(3, actual.size());
         *         assertTrue(actual.contains(client1()));
         *         assertTrue(actual.contains(client2()));
         *         assertTrue(actual.contains(manager1()));
         *
         *         assertEquals(3, usersWithFilterName1.size());
         *
         *         assertEquals(1, usersWithFilterName2.size());
         *         assertTrue(usersWithFilterName2.contains(client2()));
         */
    }

    static class ContextInitializer extends AbstractContextInitializer {
        public static final int SERVER_PORT = anAvailableRandomPort();

        @Override
        public int getServerPort() {
            return SERVER_PORT;
        }
    }
}

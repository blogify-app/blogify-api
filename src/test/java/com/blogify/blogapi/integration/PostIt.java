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
import static com.blogify.blogapi.integration.conf.PostMockData.post2;
import static com.blogify.blogapi.integration.conf.TestUtils.CLIENT1_TOKEN;
import static com.blogify.blogapi.integration.conf.TestUtils.anAvailableRandomPort;
import static com.blogify.blogapi.integration.conf.TestUtils.setUpFirebase;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

        List<Post> allPosts = api.getPosts(1, 5, null);

        List<Post> postsWithCategory1 = api.getPosts(1, 5, "category1");
        List<Post> postsWithCategory2 = api.getPosts(1, 5, "category2");

        assertEquals(2, allPosts.size());
        assertTrue(allPosts.contains(post1()));
        assertTrue(allPosts.contains(post2()));

        assertEquals(1, postsWithCategory1.size());
        assertTrue(postsWithCategory1.contains(post1()));

        assertEquals(1, postsWithCategory2.size());
        assertTrue(postsWithCategory2.contains(post2()));
    }

    static class ContextInitializer extends AbstractContextInitializer {
        public static final int SERVER_PORT = anAvailableRandomPort();

        @Override
        public int getServerPort() {
            return SERVER_PORT;
        }
    }
}

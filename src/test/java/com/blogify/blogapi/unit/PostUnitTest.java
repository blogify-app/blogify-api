package com.blogify.blogapi.unit;

import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.validator.PostValidator;
import com.blogify.blogapi.repository.PostRepository;
import com.blogify.blogapi.repository.dao.PostDao;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostUnitTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostDao postDao;

    @Mock
    private PostValidator postValidator;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById() {
        String postId = "1";
        Post post = new Post();
        post.setId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Post result = postService.getById(postId);

        assertNotNull(result);
        assertEquals(postId, result.getId());
    }

    @Test
    public void testGetPostsByUserId() {
        String userId = "user1";
        PageFromOne page = new PageFromOne(1);
        BoundedPageSize pageSize = new BoundedPageSize(10);
        List<Post> posts = new ArrayList<>();
        when(postRepository.findByUserId(anyString(), any())).thenReturn(posts);

        List<Post> result = postService.getPostsByUserId(userId, page, pageSize);

        assertNotNull(result);
        assertEquals(posts, result);
    }

    @Test
    public void testFindAllByCategory() {
        String categoryName = "category1";
        PageFromOne page = new PageFromOne(1);
        BoundedPageSize pageSize = new BoundedPageSize(10);
        List<Post> posts = new ArrayList<>();
        when(postDao.findByCriteria(anyString(), any())).thenReturn(posts);

        List<Post> result = postService.findAllByCategory(categoryName, page, pageSize);

        assertNotNull(result);
        assertEquals(posts, result);
    }

    @Test
    public void testSavePost() {
        String postId = "1";
        Post post = new Post();
        post.setId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(post);

        Post result = postService.savePost(post, postId);

        assertNotNull(result);
        assertEquals(postId, result.getId());
        assertNotNull(result.getCreationDatetime());
        verify(postValidator).accept(post);
    }

    @Test
    public void testDeletePostById() {
        String postId = "1";

        postService.deletePostById(postId);

        verify(postRepository).deleteById(postId);
    }
}

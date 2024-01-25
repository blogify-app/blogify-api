package com.blogify.blogapi.service;

import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.repository.PostRepository;
import com.blogify.blogapi.repository.dao.PostDao;
import com.blogify.blogapi.repository.model.Post;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final PostDao postDao;

  public Post getBYId(String id) {
    return postRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
  }

  public List<Post> findAllByCategory(
      String categoryName, PageFromOne page, BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
    return postDao.findByCriteria(categoryName, pageable);
  }

  public Post savePost(Post post, String postId) {
    Optional<Post> optionalPost = postRepository.findById(postId);
    if (!optionalPost.isEmpty()) {
      Post postToUpdate = optionalPost.get();
      post.setCreationDatetime(postToUpdate.getCreationDatetime());
    } else {
      post.setCreationDatetime(Instant.now());
    }
    return postRepository.save(post);
  }
}

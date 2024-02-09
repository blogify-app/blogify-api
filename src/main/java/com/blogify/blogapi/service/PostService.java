package com.blogify.blogapi.service;

import static com.blogify.blogapi.service.utils.ExceptionMessageBuilderUtils.notFoundByIdMessageException;

import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.model.validator.PostValidator;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final PostDao postDao;
  private PostValidator postValidator;
  private final String RESOURCE_NAME = "Post";

  public Post getById(String id) {

    return postRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(notFoundByIdMessageException(RESOURCE_NAME, id)));
  }

  public List<Post> getPostsByUserId(String userId, PageFromOne page, BoundedPageSize pageSize) {
    int pageValue = page != null ? page.getValue() - 1 : 0;
    int pageSizeValue = pageSize != null ? pageSize.getValue() : 10;
    Pageable pageable = PageRequest.of(pageValue, pageSizeValue);
    return postRepository.findByUserId(userId, pageable).stream().toList();
  }

  public List<Post> findAllWithPagination(PageFromOne page, BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
    return postRepository.findAll(pageable).stream().toList();
  }

  public List<Post> findAllByCategory(
      String categoryName, PageFromOne page, BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
    return postDao.findByCriteria(categoryName, pageable);
  }

  @Transactional
  public Post savePost(Post post, String postId) {
    postValidator.accept(post);
    Optional<Post> optionalPost = postRepository.findById(postId);
    if (optionalPost.isPresent()) {
      Post postToUpdate = optionalPost.get();
      post.setCreationDatetime(postToUpdate.getCreationDatetime());
      post.setThumbnailKey(postToUpdate.getThumbnailKey());
      post.setPointByView(postToUpdate.getPointByView());
    } else {
      post.setCreationDatetime(Instant.now());
      post.setPointByView(0L);
    }
    return postRepository.save(post);
  }

  @Transactional
  public Post savePostThumbnail(Post post) {
    postValidator.accept(post);
    Optional<Post> optionalPost = postRepository.findById(post.getId());
    if (optionalPost.isPresent()) {
      Post postToUpdate = optionalPost.get();
      post.setCreationDatetime(postToUpdate.getCreationDatetime());
      post.setPointByView(postToUpdate.getPointByView());
    } else {
      post.setCreationDatetime(Instant.now());
      post.setPointByView(0L);
    }
    return postRepository.save(post);
  }

  public void deletePostById(String postId) {
    postRepository.deleteById(postId);
  }
}

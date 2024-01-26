package com.blogify.blogapi.service;

import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.repository.CommentRepository;
import com.blogify.blogapi.repository.model.Comment;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;

  public Comment getBYId(String id) {
    return commentRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Comment with id " + id + " not found"));
  }

  public List<Comment> findByPostId(String postId, PageFromOne page, BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
    return commentRepository.findByPostIdOrderByCreationDatetimeDesc(postId, pageable);
  }
}

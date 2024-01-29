package com.blogify.blogapi.service;

import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.repository.CommentRepository;
import com.blogify.blogapi.repository.model.Comment;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;

  public Comment getBYId(String postId, String commentId) {
    return commentRepository
        .findByIdAndPostId(commentId, postId)
        .orElseThrow(() -> new NotFoundException("Comment with postId: " + postId + " and commentId: " + commentId + " not found"));
  }

  public List<Comment> findByPostId(String postId, PageFromOne page, BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
    return commentRepository.findByPostIdOrderByCreationDatetimeDesc(postId, pageable);
  }

  public Comment crupdateById(String postId, String commentId, Comment updatedComment) {
    Optional<Comment> existingComment = commentRepository.findByIdAndPostId(commentId, postId);
    if (existingComment.isPresent()) {
      Comment comment = existingComment.get();
      updatedComment.setCommentReactions(comment.getCommentReactions());
      updatedComment.setCreationDatetime(comment.getCreationDatetime());
    }
    return commentRepository.save(updatedComment);
  }

  public Comment deleteById(String postId, String commentId) {
      Comment deleteComment = getBYId(postId, commentId);
      commentRepository.deleteByIdAndPostId(commentId, postId);

      return deleteComment;
  }
}

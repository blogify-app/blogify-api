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

  public Comment getBYId(String commentId, String postId) {
    return commentRepository
        .findByIdAndPost_Id(commentId, postId)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "Comment with postId: "
                        + postId
                        + " and commentId: "
                        + commentId
                        + " not found"));
  }

  public List<Comment> findByPostId(String postId, PageFromOne page, BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
    return commentRepository.findByPostIdOrderByCreationDatetimeDesc(postId, pageable);
  }

  public Comment findByIdAndPostId(String commentId, String postId) {
    return commentRepository
            .findByIdAndPost_Id(commentId, postId)
            .orElseThrow(
                    () ->
                            new NotFoundException(
                                    "Entity with id " + (postId != null ? postId : commentId) + " not found"));
  }
  public Comment crupdateById(String postId, String commentId, Comment updatedComment) {
    Optional<Comment> existingComment = commentRepository.findByIdAndPost_Id(commentId, postId);
    if (existingComment.isPresent()) {
      Comment comment = existingComment.get();
      updatedComment.setCommentReactions(comment.getCommentReactions());
      updatedComment.setCreationDatetime(comment.getCreationDatetime());
    }
    return commentRepository.save(updatedComment);
  }

  // todo: lock if used
  public Comment deleteById(String commentId, String postId) {
    Comment deleteComment = getBYId(commentId, postId);
    commentRepository.deleteByIdAndPostId(commentId, postId);

    return deleteComment;
  }
}

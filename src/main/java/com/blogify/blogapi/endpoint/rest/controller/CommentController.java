package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.CommentMapper;
import com.blogify.blogapi.endpoint.mapper.ReactionMapper;
import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.Reaction;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.CommentReactionService;
import com.blogify.blogapi.service.CommentService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final CommentMapper commentMapper;
  private final CommentReactionService commentReactionService;
  private final ReactionMapper reactionMapper;

  @GetMapping("/posts/{postId}/comments")
  public List<Comment> getCommentsByPostId(
      @PathVariable String postId,
      @RequestParam(required = false) Integer page,
      @RequestParam(value = "page_size", required = false) Integer pageSize) {
    PageFromOne pageFromOne = new PageFromOne(page);
    BoundedPageSize boundedPageSize = new BoundedPageSize(pageSize);
    return commentService.findByPostId(postId, pageFromOne, boundedPageSize).stream()
        .map(
            comment ->
                commentMapper.toRest(
                    comment, commentReactionService.getReactionStat(comment.getId())))
        .toList();
  }

  @PostMapping("/posts/{postId}/comments/{commentId}/reaction")
  public Reaction reactToCommentById(
      @PathVariable String postId,
      @PathVariable String commentId,
      @RequestParam(value = "type", required = false) ReactionType type) {
    com.blogify.blogapi.repository.model.Comment comment = commentService.getBYId(commentId);
    // todo: change to user from token when it will work
    User user = comment.getUser();
    return reactionMapper.toRest(
        commentReactionService.reactAComment(comment, reactionMapper.toDomain(type), user));
  }
}

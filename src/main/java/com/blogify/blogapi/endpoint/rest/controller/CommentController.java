package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.CommentMapper;
import com.blogify.blogapi.endpoint.mapper.ReactionMapper;
import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.Reaction;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.ReactionStat;
import com.blogify.blogapi.model.Whoami;
import com.blogify.blogapi.model.validator.CommentValidator;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.CommentReactionService;
import com.blogify.blogapi.service.CommentService;
import com.blogify.blogapi.service.PostService;
import com.blogify.blogapi.service.WhoamiService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
public class CommentController {
  private final PostService postService;
  private final CommentService commentService;
  private final CommentMapper commentMapper;
  private final CommentReactionService commentReactionService;
  private final ReactionMapper reactionMapper;
  private final WhoamiService whoamiService;

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
    ;
    com.blogify.blogapi.repository.model.Comment comment =
        commentService.getBYId(commentId, postId);
    Whoami whoami = whoamiService.whoami();
    User user = whoami.getUser();
    return reactionMapper.toRest(
        commentReactionService.reactAComment(comment, reactionMapper.toDomain(type), user));
  }

  @GetMapping("/posts/{postId}/comments/{commentId}")
  public Comment getCommentById(@PathVariable String postId, @PathVariable String commentId) {
    com.blogify.blogapi.repository.model.Comment comment =
        commentService.findByIdAndPostId(commentId, postId);
    ReactionStat reactionStat = commentReactionService.getReactionStat(commentId);
    return commentMapper.toRest(comment, reactionStat);
  }

  @PutMapping("/posts/{postId}/comments/{commentId}")
  public Comment crupdateCommentById(
      @PathVariable String postId,
      @PathVariable String commentId,
      @RequestBody Comment updatedComment) {
    Post post = postService.getById(postId);
    com.blogify.blogapi.repository.model.Comment crupdatedComment =
        commentService.crupdateById(
            postId, commentId, commentMapper.toDomain(updatedComment, post));
    return commentMapper.toRest(
        crupdatedComment, commentReactionService.getReactionStat(crupdatedComment.getId()));
  }

  @DeleteMapping("/posts/{postId}/comments/{commentId}")
  public Comment deleteCommentById(@PathVariable String postId, @PathVariable String commentId) {
    com.blogify.blogapi.repository.model.Comment comment =
        commentService.deleteById(commentId, postId);
    return commentMapper.toRest(comment, commentReactionService.getReactionStat(commentId));
  }
}

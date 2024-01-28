package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.PostMapper;
import com.blogify.blogapi.endpoint.mapper.ReactionMapper;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.Reaction;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.ReactionStat;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.PostReactionService;
import com.blogify.blogapi.service.PostService;
import com.blogify.blogapi.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
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
public class PostController {
  private final PostService postService;
  private final PostMapper postMapper;
  private final UserService userService;
  private final PostReactionService postReactionService;
  private final ReactionMapper reactionMapper;

  @GetMapping("/posts")
  public List<Post> getPosts(
      @RequestParam(required = false) Integer page,
      @RequestParam(value = "page_size", required = false) Integer pageSize,
      @RequestParam(value = "categories", required = false) String categories) {
    PageFromOne pageFromOne = new PageFromOne(page);
    BoundedPageSize boundedPageSize = new BoundedPageSize(pageSize);
    return postService.findAllByCategory(categories, pageFromOne, boundedPageSize).stream()
        .map(post -> postMapper.toRest(post, postReactionService.getReactionStat(post.getId())))
        .toList();
  }

  @GetMapping("/posts/{postId}")
  public Post getPostById(@PathVariable String postId) {
    com.blogify.blogapi.repository.model.Post post = postService.getBYId(postId);
    ReactionStat reactionStat = postReactionService.getReactionStat(postId);
    return postMapper.toRest(post, reactionStat);
  }

  @PutMapping("/posts/{postId}")
  public Post putPost(@PathVariable String postId, @RequestBody Post post) {
    User author = userService.findById(post.getAuthorId());
    ReactionStat reactionStat = postReactionService.getReactionStat(postId);
    return postMapper.toRest(
        postService.savePost(postMapper.toDomain(post, author), postId), reactionStat);
  }

  @PostMapping("/posts/{postId}/reaction")
  public Reaction reactToPostById(
      @PathVariable String postId,
      @RequestParam(value = "type", required = false) ReactionType type) {
    com.blogify.blogapi.repository.model.Post post = postService.getBYId(postId);
    // todo: change to user from token when it will work
    User user = post.getUser();
    return reactionMapper.toRest(
        postReactionService.reactAPost(post, reactionMapper.toDomain(type), user));
  }

  @DeleteMapping("/posts/{postId}")
  public Post deletePostById(@PathVariable String postId) {
    Post post =
        postMapper.toRest(postService.getBYId(postId), postReactionService.getReactionStat(postId));
    postService.deletePostById(postId);
    return post;
  }
}

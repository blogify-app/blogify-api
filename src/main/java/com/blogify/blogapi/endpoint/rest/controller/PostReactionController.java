package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.ReactionMapper;
import com.blogify.blogapi.endpoint.rest.model.Reaction;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.PostReactionService;
import com.blogify.blogapi.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PostReactionController {

  private final PostReactionService postReactionService;
  private final PostService postService;
  private final ReactionMapper reactionMapper;

  @PostMapping("/posts/{postId}/reaction")
  public Reaction getPostReaction(
      @PathVariable String postId,
      @RequestParam(value = "type", required = false) ReactionType type) {
    Post post = postService.getBYId(postId);
    // todo: chage to user from token when it will work
    User user = post.getUser();
    System.out.println("/////////////////////////////////////");
    System.out.println(user);
    return reactionMapper.toRest(
        postReactionService.reactAPost(post, reactionMapper.toDomain(type), user));
  }
}

package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.PostMapper;
import com.blogify.blogapi.endpoint.mapper.ReactionMapper;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.Reaction;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.endpoint.validator.PostRestValidator;
import com.blogify.blogapi.endpoint.validator.RequestInputValidator;
import com.blogify.blogapi.endpoint.validator.RequestInputValidator.InputType;
import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.ReactionStat;
import com.blogify.blogapi.model.Whoami;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.PostReactionService;
import com.blogify.blogapi.service.PostService;
import com.blogify.blogapi.service.PostSuggestionService;
import com.blogify.blogapi.service.WhoamiService;
import java.util.List;
import java.util.stream.Collectors;
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
public class PostController {
  private final PostService postService;
  private final PostMapper postMapper;
  private final PostReactionService postReactionService;
  private final ReactionMapper reactionMapper;
  private final PostRestValidator postRestValidator;
  private final WhoamiService whoamiService;
  private final RequestInputValidator requestInputValidator;
  private final PostSuggestionService postSuggestionService;

  @GetMapping("/posts")
  public List<Post> getPosts(
      @RequestParam(required = false) PageFromOne page,
      @RequestParam(value = "page_size", required = false) BoundedPageSize pageSize,
      @RequestParam(value = "categories", required = false) String categories) {
    requestInputValidator.notNullValue(InputType.QUERY_PARAMS, "page_size", pageSize);
    requestInputValidator.notNullValue(InputType.QUERY_PARAMS, "page", page);
    return postService.findAllByCategory(categories, page, pageSize).stream()
        .map(post -> postMapper.toRest(post, postReactionService.getReactionStat(post.getId())))
        .toList();
  }

  @GetMapping("/users/{uId}/posts")
  public List<Post> getPostByUserId(
      @PathVariable("uId") String uId,
      @RequestParam(value = "page", required = false) PageFromOne page,
      @RequestParam(value = "page_size", required = false) BoundedPageSize pageSize) {
    requestInputValidator.notNullValue(InputType.QUERY_PARAMS, "page_size", pageSize);
    requestInputValidator.notNullValue(InputType.QUERY_PARAMS, "page", page);
    var posts = postService.getPostsByUserId(uId, page, pageSize);
    return posts.stream()
        .map(post -> postMapper.toRest(post, postReactionService.getReactionStat(post.getId())))
        .collect(Collectors.toList());
  }

  @PutMapping("/posts/{postId}")
  public Post putPost(@PathVariable String postId, @RequestBody Post post) {
    postRestValidator.accept(post);
    ReactionStat reactionStat = postReactionService.getReactionStat(postId);
    return postMapper.toRest(postService.savePost(postMapper.toDomain(post), postId), reactionStat);
  }

  @PostMapping("/posts/{postId}/reaction")
  public Reaction reactToPostById(
      @PathVariable String postId,
      @RequestParam(value = "type", required = false) ReactionType type) {
    requestInputValidator.notNullValue(InputType.QUERY_PARAMS, "type", type);
    com.blogify.blogapi.repository.model.Post post = postService.getById(postId);
    Whoami whoami = whoamiService.whoami();
    User user = whoami.getUser();
    return reactionMapper.toRest(
        postReactionService.reactAPost(post, reactionMapper.toDomain(type), user));
  }

  @DeleteMapping("/posts/{postId}")
  public Post deletePostById(@PathVariable String postId) {
    Post post =
        postMapper.toRest(postService.getById(postId), postReactionService.getReactionStat(postId));
    postService.deletePostById(postId);
    return post;
  }

  @GetMapping("/suggestions/posts")
  public List<Post> getSuggestedPosts(
      @RequestParam(required = false) Integer page,
      @RequestParam(value = "page_size", required = false) Integer pageSize,
      @RequestParam(value = "categories", required = false) String categories) {
    List<com.blogify.blogapi.repository.model.Post> posts =
        postService.findAllWithPagination(new PageFromOne(1), new BoundedPageSize(200));
    // todo: from token wen fix
    User user = posts.get(0).getUser();
    return postSuggestionService.getSuggestionPost(user, posts, page, pageSize, categories).stream()
        .map(post -> postMapper.toRest(post, postReactionService.getReactionStat(post.getId())))
        .toList();
  }
}

package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.UserCategoryMapper;
import com.blogify.blogapi.endpoint.mapper.UserMapper;
import com.blogify.blogapi.endpoint.mapper.ViewMapper;
import com.blogify.blogapi.endpoint.rest.model.User;
import com.blogify.blogapi.endpoint.rest.model.UserViewOnePost;
import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.UserCategory;
import com.blogify.blogapi.service.PostReactionService;
import com.blogify.blogapi.service.PostService;
import com.blogify.blogapi.service.UserService;
import com.blogify.blogapi.service.ViewService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
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
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;
  private final ViewMapper viewMapper;
  private final ViewService viewService;
  private final PostService postService;
  private final PostReactionService postReactionService;
  private final UserCategoryMapper userCategoryMapper;

  @GetMapping(value = "/users")
  public List<User> getUsers(
      @RequestParam(required = false) Integer page,
      @RequestParam(value = "page_size", required = false) Integer pageSize,
      @RequestParam(value = "name", required = false, defaultValue = "") String name) {
    PageFromOne pageFromOne = new PageFromOne(page);
    BoundedPageSize boundedPageSize = new BoundedPageSize(pageSize);
    return userService.findAllByName(name, pageFromOne, boundedPageSize).stream()
        .map(userMapper::toRest)
        .toList();
  }

  @GetMapping(value = "/users/{id}")
  public User getUserById(@PathVariable String id) {
    return userMapper.toRest(userService.findById(id));
  }

  @PutMapping(value = "/users/{id}")
  public User crupdateUser(@PathVariable(name = "id") String userId, @RequestBody User toUpdate) {
    List<UserCategory> userCategories =
        userCategoryMapper.toCategoryToUserCategory(
            toUpdate, userMapper.toDomain(toUpdate, new ArrayList<>()));
    return userMapper.toRest(
        userService.crupdateUser(userMapper.toDomain(toUpdate, userCategories), userId));
  }

  @PostMapping(value = "/users/{uid}/view/posts/{pid}")
  public UserViewOnePost userViewPost(@PathVariable String uid, @PathVariable String pid) {
    Post post = postService.getById(pid);
    return viewMapper.toRest(
        viewService.userViewPost(uid, post), postReactionService.getReactionStat(pid));
  }
}

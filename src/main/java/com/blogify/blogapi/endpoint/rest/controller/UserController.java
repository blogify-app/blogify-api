package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.UserMapper;
import com.blogify.blogapi.endpoint.rest.model.User;
import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping(value = "/users")
  public List<User> send_emails(
      @RequestParam int page,
      @RequestParam("page_size") int pageSize,
      @RequestParam(value = "name", required = false, defaultValue = "") String name) {
    PageFromOne pageFromOne = new PageFromOne(page);
    BoundedPageSize boundedPageSize = new BoundedPageSize(pageSize);
    return userService.findAllByName(name, pageFromOne, boundedPageSize).stream()
        .map(userMapper::toRest)
        .toList();
  }
}

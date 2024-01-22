package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.UserMapper;
import com.blogify.blogapi.endpoint.rest.model.User;
import com.blogify.blogapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping(value = "/users")
  public List<User> send_emails() {
    return userService.findAll().stream().map(userMapper::toRest).toList();
  }
}

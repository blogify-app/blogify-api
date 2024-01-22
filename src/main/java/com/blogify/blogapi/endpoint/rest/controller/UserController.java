package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping(value = "/users")
  public List<User> send_emails() {
    return userService.findAll();
  }
}

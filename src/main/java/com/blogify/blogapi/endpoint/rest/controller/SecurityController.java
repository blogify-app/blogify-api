package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.UserService;
import com.blogify.blogapi.service.firebase.FirebaseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SecurityController {
  private final UserService userService;
  private final FirebaseService firebaseService;

  @PostMapping("/signup")
  public User signup(@RequestBody User toCreate) {
    var user = firebaseService.createUser(toCreate, "");
    return userService.save(user);
  }

  @GetMapping("/auth")
  public String auth() {
    return "auth";
  }
}

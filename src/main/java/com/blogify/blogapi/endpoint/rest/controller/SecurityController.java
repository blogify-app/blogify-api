package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.UserMapper;
import com.blogify.blogapi.endpoint.rest.model.SignUp;
import com.blogify.blogapi.endpoint.rest.model.User;
import com.blogify.blogapi.service.UserService;
import com.blogify.blogapi.service.firebase.FirebaseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SecurityController {
  private final UserService userService;
  private final FirebaseService firebaseService;
  private final UserMapper userMapper;

  @PostMapping("/signup")
  public User signup(@RequestBody SignUp toCreate) {
    var domain = userMapper.toDomain(toCreate);
    var user = firebaseService.createUser(domain, toCreate.getPassword());
    return userMapper.toRest(userService.save(user));
  }
  

}

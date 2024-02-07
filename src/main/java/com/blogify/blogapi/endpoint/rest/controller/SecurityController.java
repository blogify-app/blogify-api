package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.UserMapper;
import com.blogify.blogapi.endpoint.mapper.WhoamiMapper;
import com.blogify.blogapi.endpoint.rest.model.AuthenticationPayload;
import com.blogify.blogapi.endpoint.rest.model.SignUp;
import com.blogify.blogapi.endpoint.rest.model.User;
import com.blogify.blogapi.endpoint.rest.model.Whoami;
import com.blogify.blogapi.model.enums.Role;
import com.blogify.blogapi.service.UserService;
import com.blogify.blogapi.service.WhoamiService;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
public class SecurityController {
  private final UserService userService;
  private final UserMapper userMapper;
  private final WhoamiService whoamiService;
  private final WhoamiMapper mapper;

  @PostMapping("/signup")
  public User signup(@RequestBody SignUp toCreate) {
    var domain =
        userMapper.toDomain(toCreate, new ArrayList<>()).toBuilder().role(Role.CLIENT).build();
    return userMapper.toRest(userService.saveSignUpUser(domain));
  }

  @PostMapping("/signin")
  public Whoami signin(@RequestBody(required = false) AuthenticationPayload payload) {
    var whoami = whoamiService.whoami();
    return mapper.toRest(whoami);
  }
}

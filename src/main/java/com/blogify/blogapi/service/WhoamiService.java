package com.blogify.blogapi.service;

import com.blogify.blogapi.endpoint.security.AuthProvider;
import com.blogify.blogapi.model.Whoami;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WhoamiService {

  public Whoami whoami() {
    var bearer = AuthProvider.getBearer();
    var user = AuthProvider.getUser();
    return Whoami.builder().id(user.getId()).bearer(bearer).user(user).build();
  }
}

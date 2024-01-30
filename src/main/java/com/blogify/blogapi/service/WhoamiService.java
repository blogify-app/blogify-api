package com.blogify.blogapi.service;

import static java.util.UUID.randomUUID;

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
    return Whoami.builder().id(randomUUID().toString()).bearer(bearer).user(user).build();
  }
}

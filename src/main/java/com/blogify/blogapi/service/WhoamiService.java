package com.blogify.blogapi.service;

import static java.util.UUID.randomUUID;

import com.blogify.blogapi.endpoint.security.AuthProvider;
import com.blogify.blogapi.model.Whoami;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WhoamiService {

  public Whoami whoami() {
    var bearer = AuthProvider.getBearer();
    var user = AuthProvider.getUser();
    log.info("Credentials {}", user);
    return Whoami.builder().id(randomUUID().toString()).bearer(bearer).user(user).build();
  }
}

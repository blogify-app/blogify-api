package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.model.User;
import com.blogify.blogapi.model.exception.ForbiddenException;
import com.blogify.blogapi.service.firebase.FirebaseService;
import com.google.auth.Credentials;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider {
  private static final String BEARER_PREFIX = "Bearer ";
  private final SecurityContext context;
  private final FirebaseService firebaseService;

  public AuthProvider(FirebaseService firebase) {
    context = SecurityContextHolder.getContext();
    firebaseService = firebase;
  }

  public static String getBearer(HttpServletRequest req) {
    String authorization = req.getHeader("Authorization");
    if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
      return authorization.substring(7);
    }
    throw new ForbiddenException("Access denied");
  }

  public User getUser() {
    Object bearer = context.getAuthentication().getPrincipal();
    return User.builder()
        .build();
  }

  public Credentials getCredentials() {
    return (Credentials) context.getAuthentication().getCredentials();
  }
}

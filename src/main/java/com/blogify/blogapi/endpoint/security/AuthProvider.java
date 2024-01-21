package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.model.exception.ForbiddenException;
import com.blogify.blogapi.service.firebase.FirebaseService;
import com.google.auth.Credentials;
import com.google.firebase.auth.FirebaseToken;
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

  // TODO: Verify if the user is database before returning
  public User getUser() {
    Object bearer = context.getAuthentication().getPrincipal();
    FirebaseToken firebaseUser = firebaseService.getUserByBearer((String) bearer);
    return User.builder().build();
  }

  public Credentials getCredentials() {
    return (Credentials) context.getAuthentication().getCredentials();
  }
}

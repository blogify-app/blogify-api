package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.model.exception.ForbiddenException;
import com.google.auth.Credentials;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider {
  private static final String BEARER_PREFIX = "Bearer ";
  private final SecurityContext context;

  public AuthProvider() {
    context = SecurityContextHolder.getContext();
  }

  public User getUser() {
    Object authUser = context.getAuthentication().getPrincipal();
    if (authUser instanceof User) {
      return (User) authUser;
    }
    return null;
  }

  public Credentials getCredentials() {
    return (Credentials) context.getAuthentication().getCredentials();
  }


  public static String getBearer(HttpServletRequest req) {
    String authorization = req.getHeader("Authorization");
    if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
      return authorization.substring(7);
    }
    throw new ForbiddenException("Access denied");
  }
}

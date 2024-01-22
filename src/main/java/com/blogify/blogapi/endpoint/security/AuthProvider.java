package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.model.exception.ForbiddenException;
import com.blogify.blogapi.repository.model.User;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider {
  private static final String BEARER_PREFIX = "Bearer ";
  private static final SecurityContext context = SecurityContextHolder.getContext();

  public static String getBearer(HttpServletRequest req) {
    String authorization = req.getHeader("Authorization");
    if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
      return authorization.substring(7);
    }
    throw new ForbiddenException("Access denied");
  }

  public static User getUser() {
    Object user = context.getAuthentication().getPrincipal();
    return (User) user;
  }
}

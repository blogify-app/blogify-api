package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.model.exception.ForbiddenException;
import com.blogify.blogapi.repository.model.User;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthProvider {
  private static final String BEARER_PREFIX = "Bearer ";

  public static String getBearer(HttpServletRequest req) {
    String authorization = req.getHeader("Authorization");
    if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
      return authorization.substring(7);
    }
    throw new ForbiddenException("Access denied");
  }


  public static User getUser() {
    SecurityContext context = SecurityContextHolder.getContext();
    Object user = context.getAuthentication().getPrincipal();
    System.out.println("000000000000000000000000000000000000");
    log.info("String user {}", user );
    return (User) user;
  }
}

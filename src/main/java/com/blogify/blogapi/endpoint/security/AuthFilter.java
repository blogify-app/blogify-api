package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.model.exception.ForbiddenException;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.UserService;
import com.blogify.blogapi.service.firebase.FirebaseService;
import com.blogify.blogapi.service.firebase.FirebaseUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

@Slf4j
public class AuthFilter extends AbstractAuthenticationProcessingFilter {
  private final FirebaseService firebaseService;
  private final UserService userService;

  protected AuthFilter(RequestMatcher requestMatcher, FirebaseService conf, UserService userSvc) {
    super(requestMatcher);
    firebaseService = conf;
    userService = userSvc;
  }

  @Override
  protected void successfulAuthentication(
          HttpServletRequest request,
          HttpServletResponse response,
          FilterChain chain,
          Authentication authenticated)
          throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authenticated);
    chain.doFilter(request, response);
  }

  @Override
  @SneakyThrows
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String token = AuthProvider.getBearer(request);
    FirebaseUser authUser = firebaseService.getUserByBearer(token);

    System.out.println("*******************************");
    System.out.println(authUser);


    if (authUser != null) {
      log.info("Authenticated user {}", authUser.getEmail());
      User user = userService.getUserByFirebaseIdAndEmail(authUser.getId(), authUser.getEmail());
      System.out.println("*******************************");
      System.out.println(user);
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(user, token);
      System.out.println("*******************************");
      System.out.println(authentication);


      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      System.out.println("*************----------------------******************");

      SecurityContextHolder.getContext().setAuthentication(authentication);
      System.out.println("*************----------***************------------******************");
      System.out.println(authentication);


      return authentication;

    }
    throw new ForbiddenException("Access denied");
  }
}

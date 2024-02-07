package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.UserService;
import com.blogify.blogapi.service.firebase.FirebaseService;
import com.blogify.blogapi.service.firebase.FirebaseUser;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
    if (token == null || token.isEmpty()) {
      throw new AuthenticationServiceException("Bearer token is missing or invalid");
    }

    FirebaseUser authUser = firebaseService.getUserByBearer(token);
    if (authUser == null) {
      throw new AuthenticationServiceException("Bearer token is expired or invalid");
    }
    log.info("Authenticated user {}", authUser.getEmail());
    User user = userService.getUserByFirebaseIdAndEmail(authUser.getId(), authUser.getEmail());
    var principal = new Principal(token, user);
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(principal, token);

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return authentication;
  }
}

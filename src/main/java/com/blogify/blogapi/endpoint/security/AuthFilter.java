package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.model.exception.ForbiddenException;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.UserService;
import com.blogify.blogapi.service.firebase.FirebaseService;
import com.google.firebase.auth.FirebaseToken;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
public class AuthFilter extends AbstractAuthenticationProcessingFilter {
  private final FirebaseService firebaseService;
  private final UserService userService;

  protected AuthFilter(RequestMatcher requestMatcher,
                       FirebaseService conf,
                       UserService userSvc) {
    super(requestMatcher);
    firebaseService = conf;
    userService = userSvc;
  }

  @Override
  @SneakyThrows
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String token = AuthProvider.getBearer(request);
    FirebaseToken authUser = firebaseService.getUserByBearer(token);
    if (authUser != null) {
      log.info("Authenticated user {}", authUser.getEmail());
      User user = userService.getUserbyFirebaseIdAndEmail(authUser.getUid(), authUser.getEmail());
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(token, user);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      return getAuthenticationManager().authenticate(authentication);
    }
    throw new ForbiddenException("Access denied");
  }
}

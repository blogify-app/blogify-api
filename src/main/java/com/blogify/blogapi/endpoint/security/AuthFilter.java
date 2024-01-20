package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.model.User;
import com.blogify.blogapi.model.exception.ForbiddenException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;


public class AuthFilter extends AbstractAuthenticationProcessingFilter {

  protected AuthFilter(RequestMatcher requestMatcher) {
    super(requestMatcher);
  }

  @Override
  @SneakyThrows
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    String token = AuthProvider.getBearer(request);
    FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
    if (decodedToken != null) {
      User authUser = toUserDetails(decodedToken);
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser.getMail(), authUser.getId());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      return getAuthenticationManager().authenticate(authentication);
    }
    throw new ForbiddenException("Access denied");
  }


  private User toUserDetails(FirebaseToken firebaseToken) {
    return User.builder()
        .id(firebaseToken.getUid())
        .firstname(firebaseToken.getName())
        .mail(firebaseToken.getEmail())
        .build();
  }

}

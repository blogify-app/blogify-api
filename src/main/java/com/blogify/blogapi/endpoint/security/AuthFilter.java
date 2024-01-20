package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.model.User;
import com.blogify.blogapi.model.exception.ForbiddenException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
  private AuthProvider authProvider;
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    verifyToken(request);
    filterChain.doFilter(request, response);
  }

  @SneakyThrows
  private void verifyToken(HttpServletRequest request){
    String token = authProvider.getBearer(request);
    FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
    if (decodedToken != null){
      User authUser = toUserDetails(decodedToken);
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser, List.of());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    throw new ForbiddenException("Access denied");
  }


  private User toUserDetails(FirebaseToken firebaseToken){
    return User.builder()
        .id(firebaseToken.getUid())
        .firstname(firebaseToken.getName())
        .mail(firebaseToken.getEmail())
        .build();
  }

}

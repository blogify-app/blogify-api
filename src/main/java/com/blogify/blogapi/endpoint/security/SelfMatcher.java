package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.repository.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class SelfMatcher implements RequestMatcher {

  private final HttpMethod method;
  private final String antPattern;
  private final String stringBeforeId;

  @Override
  public boolean matches(HttpServletRequest request) {
    AntPathRequestMatcher antMatcher = new AntPathRequestMatcher(antPattern, method.toString());
    User user  = AuthProvider.getUser();
    String userIdFromRequest = getSelfId(request);
    if (!antMatcher.matches(request)) {
      return false;
    }
    return Objects.equals(userIdFromRequest, user.getId());
  }

  private String getSelfId(HttpServletRequest request) {
    Pattern SELFABLE_URI_PATTERN = Pattern.compile(stringBeforeId + "/(?<id>[^/]+)(/.*)?");
    Matcher uriMatcher = SELFABLE_URI_PATTERN.matcher(request.getRequestURI());
    return uriMatcher.find() ? uriMatcher.group("id") : null;
  }
}

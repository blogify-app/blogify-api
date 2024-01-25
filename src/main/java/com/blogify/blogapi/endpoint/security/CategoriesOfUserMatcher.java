package com.blogify.blogapi.endpoint.security;

import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.CategoryService;
import com.blogify.blogapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class CategoriesOfUserMatcher implements RequestMatcher {
  private final UserService userService;
  private final HttpMethod method;
  private final String antPattern;

  @Override
  public boolean matches(HttpServletRequest request) {
    AntPathRequestMatcher antMatcher = new AntPathRequestMatcher(antPattern, method.toString());
    User user = AuthProvider.getUser();
    String categoriesIdFromRequest = getSelfId(request, "categories");
    if (!antMatcher.matches(request)) {
      return false;
    }
    return userService.checkUserOfCategory(
            user.getId(), categoriesIdFromRequest);
  }

  private String getSelfId(HttpServletRequest request, String stringBeforeId) {
    Pattern SELFABLE_URI_PATTERN = Pattern.compile(stringBeforeId + "/(?<id>[^/]+)(/.*)?");
    Matcher uriMatcher = SELFABLE_URI_PATTERN.matcher(request.getRequestURI());
    return uriMatcher.find() ? uriMatcher.group("id") : null;
  }
}

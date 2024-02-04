package com.blogify.blogapi.endpoint.security;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import com.blogify.blogapi.model.exception.ForbiddenException;
import com.blogify.blogapi.service.UserService;
import com.blogify.blogapi.service.firebase.FirebaseService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConf extends WebSecurityConfigurerAdapter {
  private final HandlerExceptionResolver exceptionResolver;
  private final FirebaseService firebaseService;
  private final UserService userService;
  private final AuthProvider provider;

  public SecurityConf(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
      FirebaseService firebase,
      UserService service,
      AuthProvider auth) {
    exceptionResolver = resolver;
    firebaseService = firebase;
    userService = service;
    provider = auth;
  }

  private Exception forbiddenWithRemoteInfo(Exception e, HttpServletRequest req) {
    log.info(
        String.format(
            "Access is denied for remote caller: address=%s, host=%s, port=%s",
            req.getRemoteAddr(), req.getRemoteHost(), req.getRemotePort()));
    return new ForbiddenException(e.getMessage());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .disable()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .logout()
        .disable()
        .httpBasic()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(
            (req, res, e) ->
                exceptionResolver.resolveException(req, res, null, forbiddenWithRemoteInfo(e, req)))
        .and()
        .authenticationProvider(provider)
        .addFilterBefore(
            bearerFilter(
                new NegatedRequestMatcher(
                    new OrRequestMatcher(
                        new AntPathRequestMatcher("/ping"),
                        new AntPathRequestMatcher("/health/*"),
                        new AntPathRequestMatcher("/signup"),
                        new AntPathRequestMatcher("/users", GET.name()),
                        new AntPathRequestMatcher("/users/*", GET.name()),
                        new AntPathRequestMatcher("/users/*/pictures"),
                        new AntPathRequestMatcher("/users/*/posts", GET.name()),
                        new AntPathRequestMatcher("/categories", GET.name()),
                        new AntPathRequestMatcher("/posts", GET.name()),
                        new AntPathRequestMatcher("/posts/*", GET.name()),
                        new AntPathRequestMatcher("/posts/*/comments", GET.name()),
                        new AntPathRequestMatcher("/posts/*/comments/*", GET.name()),
                        new AntPathRequestMatcher("/posts/*/pictures", GET.name()),
                        new AntPathRequestMatcher("/posts/*/pictures/*", GET.name()),
                        new AntPathRequestMatcher("/posts/*/thumbnail"),
                        new AntPathRequestMatcher("/**", OPTIONS.toString())))),
            AnonymousAuthenticationFilter.class)
        .anonymous()
        .and()
        .authorizeRequests()
        .antMatchers(OPTIONS, "/**")
        .permitAll()
        .antMatchers(GET, "/health/*")
        .permitAll()
        .antMatchers(GET, "/ping")
        .permitAll()
        .antMatchers(GET, "/whoami")
        .authenticated()
        .antMatchers(GET, "/users")
        .permitAll()
        .antMatchers(GET, "/users/*")
        .permitAll()
        .antMatchers(GET, "/users/*/posts")
        .permitAll()
        .antMatchers(GET, "/users/*/pictures")
        .permitAll()
        .antMatchers(GET, "/categories")
        .permitAll()
        .antMatchers(GET, "/posts")
        .permitAll()
        .antMatchers(GET, "/posts/*")
        .permitAll()
        .antMatchers(GET, "/posts/*/comments")
        .permitAll()
        .antMatchers(GET, "/posts/*/comments/*")
        .permitAll()
        .antMatchers(GET, "/posts/*/pictures")
        .permitAll()
        .antMatchers(GET, "/posts/*/pictures/*")
        .permitAll()
        .antMatchers(PUT, "/users/*")
        .authenticated()
        .antMatchers(PUT, "/users/*/pictures")
        .permitAll()
        .antMatchers(PUT, "/posts/*")
        .permitAll()
        .antMatchers(POST, "/posts/*/reaction")
        .authenticated()
        .antMatchers(PUT, "/posts/*/comments/*")
        .authenticated()
        .antMatchers(PUT, "/posts/*/thumbnail")
        .permitAll()
        .antMatchers(POST, "/signin")
        .authenticated()
        .antMatchers(POST, "/signup")
        .permitAll()
        .antMatchers(POST, "/posts/*/comments/*/reaction")
        .authenticated()
        .antMatchers(POST, "/posts/*/pictures/*")
        .authenticated()
        .antMatchers(DELETE, "/posts/*")
        .authenticated()
        .antMatchers(DELETE, "/posts/*/pictures/*")
        .authenticated()
        .antMatchers(DELETE, "/posts/*/comments/*")
        .authenticated()
        .antMatchers(DELETE, "/posts/*/pictures/*")
        .permitAll()
        .anyRequest()
        .denyAll()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  private AuthFilter bearerFilter(RequestMatcher requestMatcher) throws Exception {
    AuthFilter bearerFilter = new AuthFilter(requestMatcher, firebaseService, userService);
    bearerFilter.setAuthenticationManager(authenticationManager());
    bearerFilter.setAuthenticationSuccessHandler(
        (httpServletRequest, httpServletResponse, authentication) -> {});
    bearerFilter.setAuthenticationFailureHandler(
        (req, res, e) ->
            exceptionResolver.resolveException(req, res, null, forbiddenWithRemoteInfo(e, req)));
    return bearerFilter;
  }
}

package com.blogify.blogapi.endpoint.security;

import static org.springframework.http.HttpMethod.OPTIONS;

import com.blogify.blogapi.model.exception.ForbiddenException;
import com.blogify.blogapi.service.UserService;
import com.blogify.blogapi.service.firebase.FirebaseService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
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

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
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
        .configurationSource(corsConfigurationSource())
        .and()
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
                        new AntPathRequestMatcher("/signup"),
                        new AntPathRequestMatcher("/users"),
                        new AntPathRequestMatcher("/categories"),
                        new AntPathRequestMatcher("/categories"),
                        new AntPathRequestMatcher("/posts"),
                        new AntPathRequestMatcher("/posts/*"),
                        new AntPathRequestMatcher("/posts/*/reaction"),
                        new AntPathRequestMatcher("/**", OPTIONS.toString()),
                        new AntPathRequestMatcher("/users/*"),
                        new AntPathRequestMatcher("/posts/*/comments/*/reaction"),
                        new AntPathRequestMatcher("/posts/*/comments")))),
            AnonymousAuthenticationFilter.class)
        .anonymous()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/ping")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/whoami")
        .authenticated()
        .antMatchers(HttpMethod.POST, "/signup")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/users")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/users/*")
        .permitAll()
        .antMatchers(HttpMethod.PUT, "/users/*")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/categories")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/posts")
        .permitAll()
        .antMatchers(HttpMethod.PUT, "/posts/*")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/posts/*/reaction")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/posts/*/comments/*/reaction")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/posts/*/comments")
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

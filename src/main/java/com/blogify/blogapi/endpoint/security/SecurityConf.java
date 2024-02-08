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
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
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
        .authenticationEntryPoint(basicAuthenticationEntryPoint())
        .accessDeniedHandler(
            (request, response, ex) -> {
              response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
            })
        .and()
        .authenticationProvider(provider)
        .addFilterBefore(bearerFilter(), AnonymousAuthenticationFilter.class)
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
        .antMatchers(GET, "/posts/*/thumbnail")
        .permitAll()
        .antMatchers(PUT, "/categories")
        .authenticated()
        //        .antMatchers(PUT, "/posts/*")
        //        .authenticated()
        .antMatchers(POST, "/posts/*/reaction")
        .authenticated()
        .antMatchers(POST, "/signin")
        .authenticated()
        .antMatchers(POST, "/signup")
        .permitAll()
        .antMatchers(POST, "/posts/*/comments/*/reaction")
        .authenticated()
        .requestMatchers(new UserOfUserMatcher(userService, PUT, "/users/*"))
        .authenticated()
        .requestMatchers(new UserOfUserMatcher(userService, PUT, "/users/*/pictures"))
        .authenticated()
        .requestMatchers(new UserOfUserMatcher(userService, PUT, "/users/*/pictures/profile"))
        .authenticated()
        .requestMatchers(new CommentOfUserMatcher(userService, PUT, "/posts/*/comments/*"))
        .authenticated()
        .requestMatchers(new PostOfUserMatcher(userService, PUT, "/posts/*/thumbnail"))
        .authenticated()
        .requestMatchers(new PostOfUserMatcher(userService, POST, "/posts/*/pictures/*"))
        .authenticated()
        .requestMatchers(new PostOfUserMatcher(userService, DELETE, "/posts/*"))
        .authenticated()
        .requestMatchers(new PostOfUserMatcher(userService, DELETE, "/posts/*/pictures/*"))
        .authenticated()
        .requestMatchers(new PostOfUserMatcher(userService, DELETE, "/posts/*/comments/*"))
        .authenticated()
        .requestMatchers(new PostOfUserMatcher(userService, PUT, "/posts/*"))
        .authenticated()
        .anyRequest()
        .denyAll()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.addAllowedOrigin("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  private AuthFilter bearerFilter() throws Exception {
    AuthFilter bearerFilter = new AuthFilter(bearerRequestMatcher(), firebaseService, userService);
    bearerFilter.setAuthenticationManager(authenticationManager());
    bearerFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {});
    bearerFilter.setAuthenticationFailureHandler(
        (request, response, exception) -> {
          exceptionResolver.resolveException(
              request, response, null, forbiddenWithRemoteInfo(exception, request));
        });
    return bearerFilter;
  }

  private RequestMatcher bearerRequestMatcher() {
    // Define the request matcher for bearer authentication filter
    return new NegatedRequestMatcher(
        new OrRequestMatcher(
            new AntPathRequestMatcher("/ping"),
            new AntPathRequestMatcher("/health/*"),
            new AntPathRequestMatcher("/signup"),
            new AntPathRequestMatcher("/users", GET.name()),
            new AntPathRequestMatcher("/users/*", GET.name()),
            new AntPathRequestMatcher("/users/*/pictures", GET.name()),
            new AntPathRequestMatcher("/users/*/posts", GET.name()),
            new AntPathRequestMatcher("/categories"),
            new AntPathRequestMatcher("/posts", GET.name()),
            new AntPathRequestMatcher("/posts/*", GET.name()),
            new AntPathRequestMatcher("/posts/*/comments", GET.name()),
            new AntPathRequestMatcher("/posts/*/comments/*", GET.name()),
            new AntPathRequestMatcher("/posts/*/pictures", GET.name()),
            new AntPathRequestMatcher("/posts/*/pictures/*", GET.name()),
            new AntPathRequestMatcher("/posts/*/thumbnail", GET.name()),
            new AntPathRequestMatcher("/**", OPTIONS.toString())));
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

  @Bean
  public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
    BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
    entryPoint.setRealmName("Realm");
    return entryPoint;
  }
}

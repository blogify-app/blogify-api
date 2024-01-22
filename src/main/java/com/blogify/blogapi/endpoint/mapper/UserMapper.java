package com.blogify.blogapi.endpoint.mapper;

import static java.util.UUID.randomUUID;

import com.blogify.blogapi.endpoint.rest.model.Sex;
import com.blogify.blogapi.endpoint.rest.model.SignUp;
import com.blogify.blogapi.endpoint.rest.model.User;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public User toRest(com.blogify.blogapi.repository.model.User domain) {
    return new User()
        .id(domain.getId())
        .firstName(domain.getFirstname())
        .lastName(domain.getFirstname())
        .email(domain.getMail())
        .birthDate(domain.getBirthdate())
        .sex(toRestSex(domain.getSex()));
  }

  public com.blogify.blogapi.repository.model.User toDomain(SignUp rest) {
    return com.blogify.blogapi.repository.model.User.builder()
        .id(randomUUID().toString())
        .firstname(rest.getFirstName())
        .lastname(rest.getLastName())
        .birthdate(rest.getBirthDate())
        .creationDatetime(Instant.now())
        .mail(rest.getEmail())
        .build();
  }

  private Sex toRestSex(com.blogify.blogapi.model.enums.Sex sex) {
    return switch (sex) {
      case M -> Sex.M;
      case F -> Sex.F;
    };
  }
}

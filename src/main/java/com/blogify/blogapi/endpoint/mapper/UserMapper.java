package com.blogify.blogapi.endpoint.mapper;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

import com.blogify.blogapi.endpoint.rest.model.Sex;
import com.blogify.blogapi.endpoint.rest.model.SignUp;
import com.blogify.blogapi.endpoint.rest.model.User;
import com.blogify.blogapi.endpoint.rest.model.UserStatus;
import com.blogify.blogapi.model.enums.Role;
import com.blogify.blogapi.repository.model.UserCategory;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {

  private final CategoryMapper categoryMapper;

  public User toRest(com.blogify.blogapi.repository.model.User domain) {
    return new User()
        .id(domain.getId())
        .firstName(domain.getFirstname())
        .lastName(domain.getLastname())
        .email(domain.getMail())
        .birthDate(domain.getBirthdate())
        //.photoUrl(domain.getPhotoUrl())
        .bio(domain.getBio())
        //.profileBannerUrl(domain.getProfileBannerUrl())
        .username(domain.getUsername())
        .about(domain.getAbout())
        .status(convertToRest(domain.getStatus()))
        .entranceDatetime(domain.getCreationDatetime())
        .sex(convertToRest(domain.getSex()))
        .categories(domain.getUserCategories().stream().map(categoryMapper::toRest).toList());
  }

  public com.blogify.blogapi.repository.model.User toDomain(
      SignUp signUp, List<UserCategory> categories) {
    return com.blogify.blogapi.repository.model.User.builder()
        .id(signUp.getId())
        .firstname(signUp.getFirstName())
        .lastname(signUp.getLastName())
        .birthdate(signUp.getBirthDate())
        .lastUpdateDatetime(Instant.now())
        .mail(signUp.getEmail())
        .role(Role.CLIENT)
        //.photoUrl(signUp.getPhotoUrl())
        .bio(signUp.getBio())
        //.profileBannerUrl(signUp.getProfileBannerUrl())
        .username(signUp.getUsername())
        .about(signUp.getAbout())
        .status(toDomain(signUp.getStatus()))
        .sex(toDomain(signUp.getSex()))
        .userCategories(categories)
        .firebaseId(signUp.getProviderId())
        .build();
  }

  public com.blogify.blogapi.repository.model.User toDomain(
      User rest, List<UserCategory> userCategories) {
    return com.blogify.blogapi.repository.model.User.builder()
        .id(rest.getId())
        .firstname(rest.getFirstName())
        .lastname(rest.getLastName())
        .birthdate(rest.getBirthDate())
        .lastUpdateDatetime(Instant.now())
        .mail(rest.getEmail())
        //.photoUrl(rest.getPhotoUrl())
        .bio(rest.getBio())
        //.profileBannerUrl(rest.getProfileBannerUrl())
        .username(rest.getUsername())
        .about(rest.getAbout())
        .status(toDomain(rest.getStatus()))
        .sex(toDomain(rest.getSex()))
        .userCategories(userCategories)
        .build();
  }

  public static Sex convertToRest(com.blogify.blogapi.model.enums.Sex sex) {
    return mapEnum(
        sex,
        Map.of(
            com.blogify.blogapi.model.enums.Sex.M, Sex.M,
            com.blogify.blogapi.model.enums.Sex.F, Sex.F,
            com.blogify.blogapi.model.enums.Sex.OTHER, Sex.OTHER));
  }

  public static UserStatus convertToRest(com.blogify.blogapi.model.enums.UserStatus userStatus) {
    return mapEnum(
        userStatus,
        Map.of(
            com.blogify.blogapi.model.enums.UserStatus.ENABLED, UserStatus.ENABLED,
            com.blogify.blogapi.model.enums.UserStatus.BANISHED, UserStatus.BANISHED));
  }

  private com.blogify.blogapi.model.enums.Sex toDomain(Sex sex) {
    return mapEnum(
        sex,
        Map.of(
            Sex.M, com.blogify.blogapi.model.enums.Sex.M,
            Sex.F, com.blogify.blogapi.model.enums.Sex.F,
            Sex.OTHER, com.blogify.blogapi.model.enums.Sex.OTHER));
  }

  private com.blogify.blogapi.model.enums.UserStatus toDomain(UserStatus userStatus) {
    return mapEnum(
        userStatus,
        Map.of(
            UserStatus.ENABLED, com.blogify.blogapi.model.enums.UserStatus.ENABLED,
            UserStatus.BANISHED, com.blogify.blogapi.model.enums.UserStatus.BANISHED));
  }
}

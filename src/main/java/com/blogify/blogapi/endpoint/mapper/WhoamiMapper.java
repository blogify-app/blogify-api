package com.blogify.blogapi.endpoint.mapper;

import static com.blogify.blogapi.endpoint.mapper.UserMapper.convertToRest;

import com.blogify.blogapi.endpoint.rest.model.Whoami;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WhoamiMapper {
  public Whoami toRest(com.blogify.blogapi.model.Whoami model) {
    return new Whoami()
        .id(model.getId())
        .bearer(model.getBearer())
        .bio(model.getUser().getBio())
        .about(model.getUser().getAbout())
        .birthDate(model.getUser().getBirthdate())
        .email(model.getUser().getMail())
        .entranceDatetime(model.getUser().getCreationDatetime())
        .photoUrl(model.getUser().getPhotoUrl())
        .profileBannerUrl(model.getUser().getProfileBannerUrl())
        .sex(convertToRest(model.getUser().getSex()))
        .isFollowed(false)
        .addCategoriesItem(null)
        .categories(List.of())
        .username(model.getUser().getUsername());
  }
}

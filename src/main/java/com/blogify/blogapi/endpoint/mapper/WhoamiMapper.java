package com.blogify.blogapi.endpoint.mapper;

import static com.blogify.blogapi.endpoint.mapper.UserMapper.convertToRest;

import com.blogify.blogapi.constant.FileConstant;
import com.blogify.blogapi.endpoint.rest.model.Whoami;
import com.blogify.blogapi.file.S3Service;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class WhoamiMapper {
  private final S3Service s3Service;

  public Whoami toRest(com.blogify.blogapi.model.Whoami model) {
    String photoKey = model.getUser().getPhotoKey();
    String profileBannerKey = model.getUser().getProfileBannerKey();

    return new Whoami()
        .id(model.getId())
        .bearer(model.getBearer())
        .bio(model.getUser().getBio())
        .about(model.getUser().getAbout())
        .birthDate(model.getUser().getBirthdate())
        .email(model.getUser().getMail())
        .entranceDatetime(model.getUser().getCreationDatetime())
        .photoUrl(
            photoKey == null
                ? null
                : s3Service.generatePresignedUrl(photoKey, FileConstant.URL_DURATION).toString())
        .profileBannerUrl(
            profileBannerKey == null
                ? null
                : s3Service
                    .generatePresignedUrl(profileBannerKey, FileConstant.URL_DURATION)
                    .toString())
        .sex(convertToRest(model.getUser().getSex()))
        .isFollowed(false)
        .addCategoriesItem(null)
        .categories(List.of())
        .username(model.getUser().getUsername());
  }
}

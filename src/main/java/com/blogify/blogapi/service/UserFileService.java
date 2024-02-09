package com.blogify.blogapi.service;

import com.blogify.blogapi.constant.FileConstant;
import com.blogify.blogapi.endpoint.rest.model.UserPicture;
import com.blogify.blogapi.endpoint.rest.model.UserPictureType;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.repository.model.User;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserFileService {
  private final UserService userService;
  private final S3Service s3Service;

  public UserPicture uploadUserPicture(String uid, UserPictureType type, byte[] file)
      throws IOException {
    String fileBucketKey = setBucketKeyByPictureType(uid, type);
    s3Service.uploadObjectToS3Bucket(fileBucketKey, file);
    return getUserPictureWithBucketKey(uid, type, fileBucketKey);
  }

  public UserPicture getUserPicture(String uid, UserPictureType type) {
    String fileBucketKey = getBucketKeyByPictureType(uid, type);
    return getUserPictureWithBucketKey(uid, type, fileBucketKey);
  }

  private UserPicture getUserPictureWithBucketKey(
      String uid, UserPictureType type, String bucketKey) {
    String fileURL = null;
    if (bucketKey != null){
      fileURL =
              String.valueOf(s3Service.generatePresignedUrl(bucketKey, FileConstant.URL_DURATION));
    }
    UserPicture userPicture = new UserPicture();
    userPicture.setUserId(uid);
    userPicture.setType(type);
    userPicture.setUrl(fileURL);
    return userPicture;
  }

  private String getBucketKeyByPictureType(String uid, UserPictureType type) {
    User user = userService.findById(uid);
    return type == UserPictureType.PROFILE ? user.getPhotoKey() : user.getProfileBannerKey();
  }

  private String setBucketKeyByPictureType(String uid, UserPictureType type) {
    User user = userService.findById(uid);
    switch (type){
      case PROFILE -> {
        if(user.getPhotoKey() == null){
          user.setPhotoKey("user/" + uid + "/" + type.getValue());
        }
        return userService.updateUserPhotoKey(user).getPhotoKey();
      }
      case BANNER -> {
        if(user.getProfileBannerKey() == null){
          user.setProfileBannerKey("user/" + uid + "/" + type.getValue());
        }
        return userService.updateUserBannerKey(user).getProfileBannerKey();
      }
      default -> throw new BadRequestException("User picture type invalid");
      }
  }
}

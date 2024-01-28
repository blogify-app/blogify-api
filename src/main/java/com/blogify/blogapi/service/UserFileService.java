package com.blogify.blogapi.service;

import com.blogify.blogapi.constant.FileConstant;
import com.blogify.blogapi.endpoint.rest.model.UserPicture;
import com.blogify.blogapi.endpoint.rest.model.UserPictureType;
import com.blogify.blogapi.file.BucketComponent;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.repository.model.User;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class UserFileService {
  private final UserService userService;
  private final S3Service s3Service;
  private final BucketComponent bucketComponent;

  public UserPicture uploadUserPicture(String uid, UserPictureType type, MultipartFile file)
      throws IOException {
    String fileBucketKey = getBucketKeyByPictureType(uid, type);
    s3Service.uploadObjectToS3Bucket(fileBucketKey, file.getBytes());
    return getUserPictureWithBucketKey(uid, type, fileBucketKey);
  }

  public UserPicture getUserPicture(String uid, UserPictureType type) {
    String fileBucketKey = getBucketKeyByPictureType(uid, type);
    return getUserPictureWithBucketKey(uid, type, fileBucketKey);
  }

  private UserPicture getUserPictureWithBucketKey(
      String uid, UserPictureType type, String bucketKey) {
    String fileURL = String.valueOf(bucketComponent.presign(bucketKey, FileConstant.URL_DURATION));
    UserPicture userPicture = new UserPicture();
    userPicture.setUserId(uid);
    userPicture.setType(type);
    userPicture.setUrl(fileURL);
    return userPicture;
  }

  private String getBucketKeyByPictureType(String uid, UserPictureType type) {
    User user = userService.findById(uid);
    return type == UserPictureType.PROFILE ? user.getPhotoUrl() : user.getProfileBannerUrl();
  }
  ;
}

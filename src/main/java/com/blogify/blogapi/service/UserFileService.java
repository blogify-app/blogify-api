package com.blogify.blogapi.service;

import com.blogify.blogapi.endpoint.rest.model.UserPicture;
import com.blogify.blogapi.endpoint.rest.model.UserPictureType;
import com.blogify.blogapi.file.BucketComponent;
import com.blogify.blogapi.repository.model.User;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class UserFileService {
  private final UserService userService;
  private final BucketComponent bucketComponent;

  public UserPicture uploadUserPicture(String uid, UserPictureType type, MultipartFile file)
      throws IOException {
    User user = userService.findById(uid);
    String fileBucketKey = type == UserPictureType.PROFILE ? user.getPhotoUrl(): user.getProfileBannerUrl();
    File file1 = (File) file;
    bucketComponent.upload(file1, fileBucketKey);
    String fileURL = String.valueOf(bucketComponent.presign(fileBucketKey, Duration.ofMinutes(2)));
    UserPicture userPicture = new UserPicture();
    userPicture.setUserId(uid);
    userPicture.setType(type);
    userPicture.setUrl(fileURL);
    return userPicture;
  }
}

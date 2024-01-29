package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.rest.model.UserPicture;
import com.blogify.blogapi.endpoint.rest.model.UserPictureType;
import com.blogify.blogapi.service.UserFileService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class UserFileController {

  private final UserFileService service;

  @PutMapping(value = "/users/{uid}/pictures")
  public UserPicture putUserPicture(
      @PathVariable String uid,
      // TODO: handle missing params
      @RequestParam(value = "type", required = true) UserPictureType type,
      @RequestPart(value = "file", required = true) MultipartFile file)
      throws IOException {
    return service.uploadUserPicture(uid, type, file);
  }

  @GetMapping(value = "/users/{uid}/pictures")
  public UserPicture getUserPicture(
      @PathVariable String uid,
      // TODO: handle missing params
      @RequestParam(value = "type", required = true) UserPictureType type) {
    return service.getUserPicture(uid, type);
  }
}

package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.rest.model.UserPicture;
import com.blogify.blogapi.endpoint.rest.model.UserPictureType;
import com.blogify.blogapi.endpoint.validator.ParamsValidator;
import com.blogify.blogapi.endpoint.validator.ParamsValidator.InputType;
import com.blogify.blogapi.file.validator.MultipartFileValidator;
import com.blogify.blogapi.service.UserFileService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserFileController {

  private final UserFileService service;
  private final MultipartFileValidator multipartFileValidator;
  private final ParamsValidator paramsValidator;

  @PutMapping(value = "/users/{uid}/pictures")
  public UserPicture putUserPicture(
      @PathVariable String uid,
      @RequestParam(value = "type", required = false) UserPictureType type,
      @RequestPart(value = "file", required = false) MultipartFile file)
      throws IOException {

    paramsValidator.notNulValue(InputType.QUERY_PARAMS, "type", type);
    multipartFileValidator.accept(file);
    return service.uploadUserPicture(uid, type, file);
  }

  @GetMapping(value = "/users/{uid}/pictures")
  public UserPicture getUserPicture(
      @PathVariable String uid,
      @RequestParam(value = "type", required = false) UserPictureType type) {

    paramsValidator.notNulValue(InputType.QUERY_PARAMS, "type", type);
    return service.getUserPicture(uid, type);
  }
}

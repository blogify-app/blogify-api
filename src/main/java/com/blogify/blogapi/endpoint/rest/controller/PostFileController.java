package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.rest.model.PostPicture;
import com.blogify.blogapi.service.PostFileService;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class PostFileController {

  private final PostFileService service;

  @PostMapping(value = "/posts/{pid}/pictures/{picId}")
  public PostPicture uploadPostPicture(
      @PathVariable String pid,
      @PathVariable String picId,
      // TODO: handle missing params
      @RequestPart(value = "file", required = true) MultipartFile file)
      throws IOException {
    return service.uploadPicture(pid, picId, file);
  }

  @GetMapping(value = "/posts/{pid}/pictures/{picId}")
  public PostPicture getPostPictureById(@PathVariable String pid, @PathVariable String picId
      ) {
    return service.getPictureById(pid, picId);
  }

  @GetMapping(value = "/posts/{pid}/pictures")
  public List<PostPicture> getAllPostPictureById(@PathVariable String pid
      ) {
    return service.getAllPictures(pid);
  }
}

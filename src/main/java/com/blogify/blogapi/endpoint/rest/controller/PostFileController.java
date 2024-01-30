package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.PostMapper;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.PostPicture;
import com.blogify.blogapi.model.ReactionStat;
import com.blogify.blogapi.service.PostFileService;
import com.blogify.blogapi.service.PostReactionService;
import com.blogify.blogapi.service.PostService;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  private final PostService postService;
  private final PostReactionService postReactionService;
  private final PostMapper postMapper;

  @GetMapping("/posts/{postId}")
  public Post getPostById(@PathVariable String postId) {
    com.blogify.blogapi.repository.model.Post post = postService.getById(postId);
    ReactionStat reactionStat = postReactionService.getReactionStat(postId);
    String fullContent = service.getPostFullContent(post);
    return postMapper.toRest(fullContent, post, reactionStat);
  }

  @PostMapping(value = "/posts/{pid}/pictures/{picId}")
  public PostPicture uploadPostPicture(
      @PathVariable("pid") String pid,
      @PathVariable("picId") String picId,
      // TODO: handle missing params
      @RequestPart(value = "file", required = true) MultipartFile file)
      throws IOException {
    return service.uploadPicture(pid, picId, file);
  }

  @DeleteMapping(value = "/posts/{pid}/pictures/{picId}")
  public PostPicture deletePostPictureById(
      @PathVariable("pid") String pid, @PathVariable("picId") String picId) {
    return service.deletePictureById(pid, picId);
  }

  @GetMapping(value = "/posts/{pid}/pictures/{picId}")
  public PostPicture getPostPictureById(
      @PathVariable("pid") String pid, @PathVariable("picId") String picId) {
    return service.getPictureById(pid, picId);
  }

  @GetMapping(value = "/posts/{pid}/pictures")
  public List<PostPicture> getAllPostPictureById(@PathVariable("pid") String pid) {
    return service.getAllPicturesById(pid);
  }
}

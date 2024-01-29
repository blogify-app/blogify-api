package com.blogify.blogapi.service;

import static com.blogify.blogapi.service.utils.ExceptionMessageBuilderUtils.notFoundByIdMessageException;

import com.blogify.blogapi.constant.FileConstant;
import com.blogify.blogapi.endpoint.mapper.PostMapper;
import com.blogify.blogapi.endpoint.rest.model.PostPicture;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.repository.PostPictureRepository;
import com.blogify.blogapi.repository.model.Post;
import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class PostFileService {
  private final PostService postService;
  private final PostPictureRepository repository;
  private final S3Service s3Service;
  private final String RESOURCE_NAME = "Post picture";

  @Transactional
  public PostPicture uploadPicture(String pid, String picId, MultipartFile file)
      throws IOException {
    Post post = postService.getById(pid);
    String bucketKey = picId + file.getContentType();
    s3Service.uploadObjectToS3Bucket(bucketKey, file.getBytes());
    com.blogify.blogapi.repository.model.PostPicture postPicture =
        com.blogify.blogapi.repository.model.PostPicture.builder().post(post).build();
    postPicture.setId(picId);
    postPicture.setBucketKey(bucketKey);
    repository.save(postPicture);
    return getPictureWithBucketKey(postPicture);
  }

  public PostPicture getPictureById(String pid, String picId) {
    com.blogify.blogapi.repository.model.PostPicture postPicture =
        repository.findByIdAndPostId(picId, pid);
    checkIfPostPictureExist(postPicture,RESOURCE_NAME,picId);
    return getPictureWithBucketKey(postPicture);
  }

  public List<PostPicture> getAllPicturesById(String pid) {
    List<com.blogify.blogapi.repository.model.PostPicture> postPictures =
        repository.findAllByPostId(pid);
    return postPictures.stream().map(this::getPictureWithBucketKey).toList();
  }

  @Transactional
  public PostPicture deletePictureById(String pid, String picId) {
    com.blogify.blogapi.repository.model.PostPicture postPicture =
        repository.findByIdAndPostId(picId, pid);
    checkIfPostPictureExist(postPicture,RESOURCE_NAME,picId);
    PostPicture picture = deletePictureWithBucketKey(postPicture);
    repository.delete(postPicture);
   return picture;
  }
  private PostPicture deletePictureWithBucketKey(
      com.blogify.blogapi.repository.model.PostPicture postPicture) {
    s3Service.deleteS3Object(postPicture.getBucketKey());
    PostPicture picture = new PostPicture();
    picture.setId(postPicture.getId());
    picture.setPostId(postPicture.getPost().getId());
    picture.setPlaceholder(postPicture.getId());
    return picture;
  }
  private PostPicture getPictureWithBucketKey(
      com.blogify.blogapi.repository.model.PostPicture postPicture) {
    PostPicture picture = new PostPicture();
    picture.setId(postPicture.getId());
    picture.setPostId(postPicture.getPost().getId());
    picture.setUrl(
        String.valueOf(
            s3Service.generatePresignedUrl(postPicture.getBucketKey(), FileConstant.URL_DURATION)));
    picture.setPlaceholder(postPicture.getId());
    return picture;
  }

  private void checkIfPostPictureExist(com.blogify.blogapi.repository.model.PostPicture postPicture, String resource, String id){
    if (postPicture == null) {
      throw new NotFoundException(notFoundByIdMessageException(resource, id));
    }
  }

  public String getPostFullContent(Post post){
    List<PostPicture> postPictures = getAllPicturesById(post.getId());
    return replacePlaceholders(post.getContent(),postPictures);
  }
  private String replacePlaceholders(String htmlContent, List<PostPicture> postPictures) {

    for (PostPicture picture : postPictures) {
      String placeholder = "{{"+picture.getId()+"}}";
      htmlContent = htmlContent.replace(placeholder, picture.getUrl()!=null? picture.getUrl() : "");
    }
    return htmlContent;
  }
}

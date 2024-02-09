package com.blogify.blogapi.service;

import static com.blogify.blogapi.service.utils.ExceptionMessageBuilderUtils.notFoundByIdMessageException;

import com.blogify.blogapi.constant.FileConstant;
import com.blogify.blogapi.endpoint.rest.model.PostPicture;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.repository.PostPictureRepository;
import com.blogify.blogapi.repository.model.Post;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostFileService {
  private final PostService postService;
  private final PostPictureRepository repository;
  private final S3Service s3Service;
  private final String RESOURCE_NAME = "Post picture";

  @Transactional
  public PostPicture uploadPicture(String pid, String picId, byte[] file)
      throws IOException {
    if (repository.findById(picId).isPresent()) {
      throw new BadRequestException("Post picture with id " + picId + " already exists");
    }
    String extension = "jpg";
        //Objects.requireNonNull(file.getContentType())
        //   .substring(file.getContentType().lastIndexOf("/") + 1);
    Post post = postService.getById(pid);
    String bucketKey = picId + "." + extension;
    s3Service.uploadObjectToS3Bucket(bucketKey, file);
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
    checkIfPostPictureExist(postPicture, picId);
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
    checkIfPostPictureExist(postPicture, picId);
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

  private void checkIfPostPictureExist(
          com.blogify.blogapi.repository.model.PostPicture postPicture, String id) {
    if (postPicture == null) {
      throw new NotFoundException(notFoundByIdMessageException(RESOURCE_NAME, id));
    }
  }

  public String getPostFullContent(Post post) {
    List<PostPicture> postPictures = getAllPicturesById(post.getId());
    return replacePlaceholders(post.getContent(), postPictures);
  }

  private String replacePlaceholders(String htmlContent, List<PostPicture> postPictures) {

    for (PostPicture picture : postPictures) {
      String placeholder = "{{" + picture.getId() + "}}";
      htmlContent =
          htmlContent.replace(placeholder, picture.getUrl() != null ? picture.getUrl() : "");
    }
    return htmlContent;
  }

  public Post uploadPostThumbnail(String pid, byte[] file) throws IOException {
    String fileBucketKey = setBucketThumbnailKeyByPostId(pid);
    s3Service.uploadObjectToS3Bucket(fileBucketKey, file);
    URL url = s3Service.generatePresignedUrl(fileBucketKey, FileConstant.URL_DURATION);
    Post post = postService.getById(pid);
    post.setThumbnailKey(url.toString());
    return post;
  }

  private String setBucketThumbnailKeyByPostId(String pid) {
    Post post = postService.getById(pid);
    if (post.getThumbnailKey() == null) {
      post.setThumbnailKey("post/" + pid + "/" + "thumbnail");
    }
    return postService.savePostThumbnail(post).getThumbnailKey();
  }
}

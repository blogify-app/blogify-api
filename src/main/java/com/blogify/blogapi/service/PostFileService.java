package com.blogify.blogapi.service;

import static com.blogify.blogapi.service.utils.ExceptionMessageBuilderUtils.notFoundByIdMessageException;

import com.blogify.blogapi.endpoint.rest.model.PostPicture;
import com.blogify.blogapi.file.BucketComponent;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.repository.PostPictureRepository;
import com.blogify.blogapi.repository.model.Post;
import java.io.IOException;
import java.time.Duration;
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
  private final BucketComponent bucketComponent;

  @Transactional
  public PostPicture uploadPicture(String pid, String picId, MultipartFile file)
      throws IOException {
    Post post = postService.getBYId(pid);
    String bucketKey = picId+file.getContentType();
    s3Service.uploadObjectToS3Bucket(bucketKey, file.getBytes());
    com.blogify.blogapi.repository.model.PostPicture postPicture = com.blogify.blogapi.repository.model.PostPicture.builder()
        .post(post)
        .build();
    postPicture.setId(picId);
    postPicture.setBucketKey(bucketKey);
    repository.save(postPicture);
    return getPictureWithBucketKey(postPicture);
  }

  public PostPicture getPicture(String pid, String picId) {
    com.blogify.blogapi.repository.model.PostPicture postPicture = repository.findByIdAndPostId(picId,pid);
    if (postPicture == null){
      throw new NotFoundException(notFoundByIdMessageException("Post picture",picId));
    }
    return getPictureWithBucketKey(postPicture);
  }

  public List<PostPicture> getAllPictures(String pid) {
    List<com.blogify.blogapi.repository.model.PostPicture> postPictures = repository.findAllByPostId(pid);
    return postPictures.stream().map(this::getPictureWithBucketKey).toList();
  }
  private PostPicture getPictureWithBucketKey(com.blogify.blogapi.repository.model.PostPicture postPicture) {
    PostPicture picture = new PostPicture();
    picture.setId(postPicture.getId());
    picture.setPostId(postPicture.getPost().getId());
    picture.setUrl(String.valueOf(bucketComponent.presign(postPicture.getBucketKey(), Duration.ofMinutes(2))));
    picture.setPlaceholder(postPicture.getId());
    return picture;
  }

}

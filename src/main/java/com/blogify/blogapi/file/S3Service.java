package com.blogify.blogapi.file;

import com.blogify.blogapi.model.exception.ApiException;
import java.net.URL;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

  private final BucketComponent bucketComponent;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  private final S3Client s3Client;

  public URL generatePresignedUrl(String bucketKey, Duration urlDuration) {
    return bucketComponent.presign(bucketKey, urlDuration);
  }

  public String uploadObjectToS3Bucket(String key, byte[] file) {
    PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();
    try {
      s3Client.putObject(objectRequest, RequestBody.fromBytes(file));
      return key;
    } catch (AwsServiceException | SdkClientException e) {
      throw s3ErrorHandler(e);
    }
  }

  public String deleteS3Object(String key) {
    DeleteObjectRequest deleteObjectsRequest =
        DeleteObjectRequest.builder().bucket(bucketName).key(key).build();
    try {
      s3Client.deleteObject(deleteObjectsRequest);
      return key;
    } catch (AwsServiceException | SdkClientException e) {
      throw s3ErrorHandler(e);
    }
  }

  /*
  public byte[] getObjectFromS3Bucket(String key) {
    GetObjectRequest objectRequest = GetObjectRequest.builder().bucket(bucketName).key(key).build();
    try {
      ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
      return objectBytes.asByteArray();
    } catch (AwsServiceException | SdkClientException e) {
      s3ErrorHandler(e);
      return new byte[0];
    }
  }
   */

  private ApiException s3ErrorHandler(Exception e) {
    log.error("S3 error:", e);
    int statusCode = 500;
    if (e instanceof AwsServiceException awsException) {
      statusCode = awsException.statusCode();
    }
    if (400 <= statusCode && statusCode < 500) {
      throw new ApiException(ApiException.ExceptionType.CLIENT_EXCEPTION, e.getMessage());
    } else {
      throw new ApiException(ApiException.ExceptionType.SERVER_EXCEPTION, e.getMessage());
    }
  }
}

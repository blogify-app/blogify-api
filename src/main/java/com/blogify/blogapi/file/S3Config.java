package com.blogify.blogapi.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  @Value("${aws.region}")
  private String awsS3Region;

  @Bean
  public S3Client amazonS3Client() {
    return S3Client.builder()
        .region(Region.of(awsS3Region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();
  }
}

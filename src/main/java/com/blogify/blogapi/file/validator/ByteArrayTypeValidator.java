package com.blogify.blogapi.file.validator;

import com.blogify.blogapi.model.exception.BadRequestException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Component;

@Component
public class ByteArrayTypeValidator {
  public boolean isValid(byte[] bytes, String contentType) {
    try {
      DefaultDetector detector = new DefaultDetector();
      Metadata metadata = new Metadata();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
      MediaType mediaType = detector.detect(inputStream, metadata);
      return contentType.startsWith(mediaType.getType()) && contentType.endsWith(mediaType.getSubtype());
    } catch (IOException ignored) {
    }
    return false;
  }

  public void accept(byte[] bytes, String contentType){
    if (!isValid(bytes, contentType)){
      throw new BadRequestException("Fife type not supported");
    }
  }
}

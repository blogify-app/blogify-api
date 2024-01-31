package com.blogify.blogapi.file.validator;

import com.blogify.blogapi.model.exception.BadRequestException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;

public class ImageValidator {
  public static void isValid(byte[] pdfData, String type, String subType) {
    try {
      DefaultDetector detector = new DefaultDetector();
      Parser parser = new AutoDetectParser(detector);
      Metadata metadata = new Metadata();
      ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfData);
      MediaType mediaType = detector.detect(inputStream, metadata);

      boolean isValid = type.equals(mediaType.getType()) && subType.equals(mediaType.getSubtype());
      if (!isValid) {
        throw new BadRequestException("file is not a valid " + subType);
      }
    } catch (IOException ignored) {
    }
  }
}

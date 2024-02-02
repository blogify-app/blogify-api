package com.blogify.blogapi.file.validator.utils;

import com.blogify.blogapi.model.exception.BadRequestException;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ImageExtensionValidator implements FileExtensionValidator {
  private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

  @Override
  public boolean isValidFile(String filename) {
    String lowerCaseFilename = filename.toLowerCase();
    return endsWithValidExtension(lowerCaseFilename);
  }

  @Override
  public void accept(String filename) {
    if (!isValidFile(filename)) {
      throw new BadRequestException("File type not supported");
    }
  }

  private boolean endsWithValidExtension(String filename) {
    for (String extension : ALLOWED_EXTENSIONS) {
      if (filename.endsWith("." + extension)) {
        return true;
      }
    }
    return false;
  }
}

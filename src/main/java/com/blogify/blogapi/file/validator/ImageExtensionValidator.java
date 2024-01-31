package com.blogify.blogapi.file.validator;

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

  private boolean endsWithValidExtension(String filename) {
    for (String extension : ALLOWED_EXTENSIONS) {
      if (filename.endsWith("." + extension)) {
        return true;
      }
    }
    return false;
  }

}

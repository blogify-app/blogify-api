package com.blogify.blogapi.file.validator.utils;

import com.blogify.blogapi.model.exception.BadRequestException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileValidator implements Consumer<MultipartFile> {

  // Maximum file size (1MB)
  private static final long MAX_FILE_SIZE = 1 * 1024 * 1024;

  @Override
  public void accept(MultipartFile file) {
    Set<String> violationMessages = new HashSet<>();
    if (file == null || file.isEmpty()) {
      violationMessages.add("File is mandatory");
    } else if (file.getSize() > MAX_FILE_SIZE) {
      violationMessages.add("File size must be less than " + MAX_FILE_SIZE / (1024 * 1024) + " MB");
    }

    if (!violationMessages.isEmpty()) {
      String formattedViolationMessages =
          violationMessages.stream().map(String::toString).collect(Collectors.joining(". "));
      throw new BadRequestException(formattedViolationMessages);
    }
  }
}

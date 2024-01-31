package com.blogify.blogapi.file.validator;

public interface FileExtensionValidator {
  boolean isValidFile(String filename);
  void accept(String filename);
}

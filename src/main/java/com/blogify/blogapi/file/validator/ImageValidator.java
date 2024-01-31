package com.blogify.blogapi.file.validator;

import java.io.IOException;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ImageValidator {
  private final ImageExtensionValidator extensionValidator;
  private final MultipartFileValidator multipartFileValidator;
  private final ByteArrayTypeValidator byteArrayTypeValidator;
  public void accept(MultipartFile file) throws IOException {
    multipartFileValidator.accept(file);
    extensionValidator.accept(Objects.requireNonNull(file.getOriginalFilename()));
    byteArrayTypeValidator.accept(file.getBytes(),file.getContentType());
  }

}

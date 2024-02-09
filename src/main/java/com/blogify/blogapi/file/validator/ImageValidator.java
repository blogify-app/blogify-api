package com.blogify.blogapi.file.validator;

import com.blogify.blogapi.file.validator.utils.ByteArrayTypeValidator;
import com.blogify.blogapi.file.validator.utils.ImageExtensionValidator;
import com.blogify.blogapi.file.validator.utils.MultipartFileValidator;
import com.blogify.blogapi.model.exception.BadRequestException;
import java.io.IOException;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ImageValidator implements Consumer<MultipartFile> {
  private final ImageExtensionValidator imageExtensionValidator;
  private final MultipartFileValidator multipartFileValidator;
  private final ByteArrayTypeValidator byteArrayTypeValidator;

  @Override
  public void accept(MultipartFile file) {
    byte[] imageBytes;
    try {
      imageBytes = file.getBytes();
    } catch (IOException e) {
      throw new BadRequestException("Fife type not supported, when change to byte.");
    }
    multipartFileValidator.accept(file);
    byteArrayTypeValidator.accept(imageBytes, file.getContentType());
  }

  public void accept(byte[] file) {
    byte[] imageBytes;
    imageBytes = file;
    byteArrayTypeValidator.accept(imageBytes, "image");
  }
}

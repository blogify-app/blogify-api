package com.blogify.blogapi.file.validator;

import com.blogify.blogapi.file.validator.utils.ByteArrayTypeValidator;
import com.blogify.blogapi.model.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ImageValidator {
  private final ByteArrayTypeValidator byteArrayTypeValidator;

  public static final int FILE_MAX_SIZE_AS_MB = 2;

  public void accept(byte[] file) {
    if (file.length > FILE_MAX_SIZE_AS_MB * 1024 * 1024) { // 2MB in bytes
      throw new BadRequestException("FIle size must be less than "+FILE_MAX_SIZE_AS_MB+"MB");
    }
    byteArrayTypeValidator.accept(file, "image");
  }

  private byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
          + Character.digit(s.charAt(i+1), 16));
    }
    return data;
  }

}

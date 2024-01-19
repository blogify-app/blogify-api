package com.blogify.blogapi.service.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataFormatterUtils {
  public static <T extends Enum<T>> T fromValue(Class<T> enumClass, String value) {
    for (T enumConstant : enumClass.getEnumConstants()) {
      if (enumConstant.name().equals(value)) {
        return enumConstant;
      }
    }
    throw new IllegalArgumentException(
        "Unexpected value '" + value + "' for enum " + enumClass.getSimpleName());
  }
}

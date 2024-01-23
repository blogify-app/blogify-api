package com.blogify.blogapi.service.utils;

import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EnumMapperUtils {
  public static <T, U> U mapEnum(T input, Map<T, U> enumMap) {
    if (input == null) {
      return null;
    }
    return enumMap.get(input);
  }
}

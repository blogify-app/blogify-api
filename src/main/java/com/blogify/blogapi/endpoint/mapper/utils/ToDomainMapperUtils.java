package com.blogify.blogapi.endpoint.mapper.utils;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ToDomainMapperUtils {
  public <T> List<T> checkNullList(List<T> list) {
    return list != null ? list : Collections.emptyList();
  }
}

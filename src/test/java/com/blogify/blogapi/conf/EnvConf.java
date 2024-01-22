package com.blogify.blogapi.conf;

import org.springframework.test.context.DynamicPropertyRegistry;

public class EnvConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "firebase.private.key",
        () ->
            "dummy");
  }
}

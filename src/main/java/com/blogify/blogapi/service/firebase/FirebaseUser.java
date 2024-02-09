package com.blogify.blogapi.service.firebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
@AllArgsConstructor
public class FirebaseUser {
  private String email;
  private String id;
}

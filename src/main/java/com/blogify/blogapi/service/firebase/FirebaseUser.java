package com.blogify.blogapi.service.firebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
@AllArgsConstructor
public class FirebaseUser {
  private String email;
  private String id;
}

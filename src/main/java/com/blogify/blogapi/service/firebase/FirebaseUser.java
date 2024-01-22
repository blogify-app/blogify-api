package com.blogify.blogapi.service.firebase;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@Builder
public class FirebaseUser {
  private String email;
  private String id;
}

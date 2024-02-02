package com.blogify.blogapi.model;

import com.blogify.blogapi.repository.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Whoami {
  private String id;
  private String bearer;
  private User user;
}

package com.blogify.blogapi.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class User implements Serializable {
  private String id;
  private String firstname;
  private String lastname;
  private String mail;
  private LocalDate birthdate;
  private String role;
  private Instant creationDatetime;
  private Instant lastUpdateDatetime;
}
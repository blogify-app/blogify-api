package com.blogify.blogapi.model;


import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class Category implements Serializable {
  private Integer id;
  private String name;
  private Instant creationDatetime;
}
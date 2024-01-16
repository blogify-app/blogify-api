package com.blogify.blogapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;

@Data
@ToString
@Builder
@AllArgsConstructor
public class Post implements Serializable {
    private String id;
    private String title;
    private String content;
    private Instant creationDatetime;
    private Instant lastUpdateDatetime;
}

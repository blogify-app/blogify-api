package com.blogify.blogapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"post\"")
public class Post implements Serializable {
    @Id
    private String id;

    private String title;

    private String content;

    @CreationTimestamp
    private Instant creationDatetime;

    private Instant lastUpdateDatetime;
}

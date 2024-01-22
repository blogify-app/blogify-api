package com.blogify.blogapi.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

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

    @OneToMany
    @JoinColumn(name = "id_post")
    private List<PostReaction> postReactions;
    //TODO: remove this , just test

    @CreationTimestamp
    private Instant creationDatetime;

    private Instant lastUpdateDatetime;
}

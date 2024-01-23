package com.blogify.blogapi.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post implements Serializable {
    @Id
    private String id;

    /**
     * @ManyToOne
     * @JoinColumn(name = "user_id")
     * private User user;
     */

    private String title;
    private String content;
    private String thumbnailUrl;
    private String description;
    private postStatus status;

    @CreationTimestamp
    private Instant creationDatetime;
    private Instant lastUpdateDatetime;

    /**
     * @OneToMany
     * private List<Category> categories;
     */

    @OneToMany(
        mappedBy = "post",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PostReaction> postReactions;
    public enum postStatus{
        ARCHIVED,
        DRAFT,
        DISABLED;
    }
}

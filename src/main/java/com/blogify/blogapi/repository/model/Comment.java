package com.blogify.blogapi.repository.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "\"comment\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Comment implements Serializable {
    @Id
    private String id;

    private String content;

    @CreationTimestamp
    private Instant creationDatetime;

    /* @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    // TODO: Add relation with post, waiting for the completion of the Post entity */

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_id")
    private List<CommentReaction> reactions;
}

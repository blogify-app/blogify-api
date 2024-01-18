package com.blogify.blogapi.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Comment {
    @Id
    private UUID id;

    @Column(name = "content")
    private String content;

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    /* @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    // TODO: Add relation with post, waiting for the completion of the Post entity */

    @OneToMany(mappedBy = "comment")
    private List<CommentReaction> commentReaction;
}

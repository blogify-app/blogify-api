package com.blogify.blogapi.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment_reacts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentReact {
    @Id
    private UUID id;

    /* @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // TODO: Add relation with User, waiting for the completion of the User entity */

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(name = "is_like")
    private boolean isLike;
}


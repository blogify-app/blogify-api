package com.blogify.blogapi.repository.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comment_reacts")
@Data
public class CommentReact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;*/

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(name = "is_like")
    private boolean isLike;
}


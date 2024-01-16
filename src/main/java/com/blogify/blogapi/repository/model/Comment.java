package com.blogify.blogapi.repository.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    /*@ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment")
    private List<CommentReact> commentReacts;*/
}

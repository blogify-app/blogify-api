package com.blogify.blogapi.model;

import com.blogify.blogapi.model.type.ReactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"post_reaction\"")
public class PostReaction implements Serializable {
    @Id
    private String id;

    //TODO: add relation with user

    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post post;

    @Column(name = "type")
    private ReactionTypeEnum reactionType;
}

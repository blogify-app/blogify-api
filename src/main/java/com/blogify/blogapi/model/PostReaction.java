package com.blogify.blogapi.model;

import com.blogify.blogapi.model.type.PostgresqlEnum;
import com.blogify.blogapi.model.type.ReactionTypeEnum;
import com.blogify.blogapi.service.PostReactionService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"post_reaction\"")
@TypeDef(name = "psql_enum",typeClass = PostgresqlEnum.class)
public class PostReaction implements Serializable {
    @Id
    private String id;

    //TODO: add relation with user

    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post post;

    @Type(type = "psql_enum")
    @Enumerated(EnumType.STRING)
    private ReactionTypeEnum reactionType;
}

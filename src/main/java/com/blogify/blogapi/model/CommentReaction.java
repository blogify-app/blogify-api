package com.blogify.blogapi.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "\"comment_reaction\"")
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CommentReaction extends Reaction{
    public void setComment(Comment comment) {
    }

    public void setReactionType(ReactionType reactionType) {
    }
}


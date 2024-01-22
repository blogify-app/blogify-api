package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.repository.model.Comment;
import com.blogify.blogapi.repository.model.Reaction;
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
public class CommentReaction extends Reaction {
}


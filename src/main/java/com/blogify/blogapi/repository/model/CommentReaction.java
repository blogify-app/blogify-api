package com.blogify.blogapi.repository.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "\"comment_reaction\"")
@Data
@Builder
@AllArgsConstructor
public class CommentReaction extends Reaction {
}


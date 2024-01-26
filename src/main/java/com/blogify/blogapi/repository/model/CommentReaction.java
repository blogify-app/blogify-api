package com.blogify.blogapi.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("COMT")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReaction extends Reaction implements Serializable {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id")
  @JsonIgnore
  private Comment comment;
}

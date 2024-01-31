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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("COMT")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReaction extends Reaction implements Serializable {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id")
  @JsonIgnore
  private Comment comment;
}

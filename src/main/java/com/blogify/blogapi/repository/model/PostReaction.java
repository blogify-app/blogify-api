package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.model.enums.ReactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"post_reaction\"")
public class PostReaction extends Reaction implements Serializable {

  public PostReaction(
      String id, User user, Instant creationDatetime, ReactionType type, Post post) {
    super(id, user, creationDatetime, type);
    this.post = post;
  }
  ;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  @JsonIgnore
  private Post post;
}

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("POST")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostReaction extends Reaction implements Serializable {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  @JsonIgnore
  private Post post;
}

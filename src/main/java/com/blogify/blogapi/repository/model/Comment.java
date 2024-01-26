package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.model.enums.CommentStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "\"comment\"")
@EqualsAndHashCode
@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
  @Id private String id;

  private String content;

  @CreationTimestamp private Instant creationDatetime;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String replyToId;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @OneToMany
  @JoinColumn(name = "reaction_id")
  private List<CommentReaction> commentReactions;

  @Enumerated(EnumType.STRING)
  private CommentStatus status;
}
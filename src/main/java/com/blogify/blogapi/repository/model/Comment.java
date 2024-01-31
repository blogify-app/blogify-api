package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.model.enums.CommentStatus;
import com.blogify.blogapi.repository.types.PostgresEnumType;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "\"comment\"")
@EqualsAndHashCode
@Builder
@Getter
@Setter
@SQLDelete(sql = "UPDATE Comment SET deleted = true WHERE id=?")
@TypeDef(name = "pgsql_enum", typeClass = PostgresEnumType.class)
@Where(clause = "deleted=false")
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
  @Type(type = "pgsql_enum")
  private CommentStatus status;

  private boolean deleted = Boolean.FALSE;
}

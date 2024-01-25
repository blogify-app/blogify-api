package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.model.enums.ReactionType;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "\"reaction\"")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class Reaction {
  @Id private String id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @CreationTimestamp private Instant creationDatetime;

  @Enumerated(EnumType.STRING)
  private ReactionType type;
}

package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.model.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post implements Serializable {
  @Id private String id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  private String title;
  private String content;
  private String thumbnailUrl;
  private String description;

  @Enumerated(EnumType.STRING)
  private PostStatus status;

  @CreationTimestamp private Instant creationDatetime;
  @UpdateTimestamp private Instant lastUpdateDatetime;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private List<PostCategory> postCategories;

  @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonIgnore
  private List<PostReaction> postReactions;
}

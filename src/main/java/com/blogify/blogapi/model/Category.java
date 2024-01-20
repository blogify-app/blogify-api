package com.blogify.blogapi.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@EqualsAndHashCode
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"category\"")
public class Category implements Serializable {
  @Id
  private String id;
  private String name;
  @CreationTimestamp
  @Getter(AccessLevel.NONE)
  private Instant creationDatetime;
  @OneToMany(mappedBy = "category")
  private List<UserCategory> userCategories;
}

package com.blogify.blogapi.repository.model;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
  @Id private String id;
  private String name;
  @CreationTimestamp private Instant creationDatetime;
}

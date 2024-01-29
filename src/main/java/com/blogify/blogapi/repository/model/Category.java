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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@EqualsAndHashCode
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE Category SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(name = "\"category\"")
public class Category implements Serializable {
  @Id private String id;
  private String name;
  @CreationTimestamp private Instant creationDatetime;

  private boolean deleted = Boolean.FALSE;
}

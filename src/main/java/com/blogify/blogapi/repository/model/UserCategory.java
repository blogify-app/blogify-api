package com.blogify.blogapi.repository.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"user_category\"")
public class UserCategory implements Serializable {
  @Id private String id;

  @JoinColumn(referencedColumnName = "id")
  private String userId;

  @JoinColumn(referencedColumnName = "id")
  private String categoryId;
}

package com.blogify.blogapi.repository.model;

import java.time.Instant;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "disc_type", length = 4)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Picture {

  @Id private String id;

  private String bucketKey;

  @CreationTimestamp private Instant creationDatetime;
}

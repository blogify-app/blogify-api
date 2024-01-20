package com.blogify.blogapi.model;

import com.blogify.blogapi.model.enums.Role;
import com.blogify.blogapi.model.enums.Sex;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
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
@Table(name = "\"user\"")
public class User implements Serializable {
  @Id
  private String id;
  private String firstname;
  private String lastname;
  private String mail;
  private LocalDate birthdate;
  private Role role;
  private Sex sex;
  @CreationTimestamp
  @Getter(AccessLevel.NONE)
  private Instant creationDatetime;
  private Instant lastUpdateDatetime;
  @OneToMany(mappedBy = "user")
  private List<UserCategory> userCategories;
}


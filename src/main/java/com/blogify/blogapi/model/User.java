package com.blogify.blogapi.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.blogify.blogapi.service.utils.DataFormatterUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
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
  @ManyToMany
  @JoinTable(
      name = "user_category",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private List<Category> categories;

  public enum Role {
    MANAGER,
    CLIENT;

    public static Role fromValue(String value) {
      return DataFormatterUtils.fromValue(Role.class, value);
    }
  }

  public enum Sex {
    M,
    F;

    public static Sex fromValue(String value) {
      return DataFormatterUtils.fromValue(Sex.class, value);
    }
  }

}
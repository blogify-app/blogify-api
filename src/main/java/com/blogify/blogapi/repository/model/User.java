package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.model.enums.Role;
import com.blogify.blogapi.model.enums.Sex;
import com.blogify.blogapi.model.enums.UserStatus;
import com.blogify.blogapi.repository.types.PostgresEnumType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

@Data
@EqualsAndHashCode
@ToString
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "pgsql_enum", typeClass = PostgresEnumType.class)
@SQLDelete(sql = "UPDATE User SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(name = "\"user\"")
public class User implements Serializable {
  @Id private String id;
  private String firstname;
  private String lastname;

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email must be valid")
  private String mail;

  private LocalDate birthdate;
  private String firebaseId;

  @Enumerated(EnumType.STRING)
  @Type(type = "pgsql_enum")
  private Role role;

  @Enumerated(EnumType.STRING)
  @Type(type = "pgsql_enum")
  private Sex sex;

  @CreationTimestamp private Instant creationDatetime;
  private Instant lastUpdateDatetime;

  private String photoUrl;
  private String bio;
  private String profileBannerUrl;
  private String username;
  private String about;

  @Enumerated(EnumType.STRING)
  @Type(type = "pgsql_enum")
  private UserStatus status;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private List<UserCategory> userCategories;

  private boolean deleted = Boolean.FALSE;
}

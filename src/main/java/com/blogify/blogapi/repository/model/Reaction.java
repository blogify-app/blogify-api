package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.model.enums.ReactionType;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

@Entity(name = "\"reaction\"")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class Reaction {
    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Type(type="pqsql_enum")
    private ReactionType type;

    @CreationTimestamp
    private Instant creationDatetime;

    private String userId;
    private String postId;
    private String commentId;
}

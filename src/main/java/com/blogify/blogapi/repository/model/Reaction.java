package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.service.utils.DataFormatterUtils;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "\"reaction\"")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class Reaction {
    @Id
    private String id;

    private ReactionType type;

    @CreationTimestamp
    private Instant creationDatetime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    /*@ManyToOne
    @JoinColumn(name = "post_id")
    private Post postId;*/

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum ReactionType {
        LIKE,
        DISLIKE;

        public static ReactionType fromValue(String value) {
            return DataFormatterUtils.fromValue(ReactionType.class, value);
        }

    }
}

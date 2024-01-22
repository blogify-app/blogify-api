package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.service.utils.DataFormatterUtils;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "\"reaction\"")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class Reaction {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private ReactionType type;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static enum ReactionType {
        LIKE,
        DISLIKE;

        public static ReactionType fromValue(String value) {
            return DataFormatterUtils.fromValue(ReactionType.class, value);
        }

    }
}

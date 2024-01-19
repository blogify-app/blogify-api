package com.blogify.blogapi.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Reaction {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType; //TODO 6: rename to: type

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum ReactionType { // TODO 7: add static to it because it's relate tu the class not an object
        LIKE, DISLIKE
    }
}

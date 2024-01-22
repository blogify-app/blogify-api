package com.blogify.blogapi.repository.model;

import com.blogify.blogapi.service.utils.DataFormatterUtils;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "\"comment\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {
    @Id
    private String id;

    private String content;

    @CreationTimestamp
    private Instant creationDatetime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reply_to_id")
    private String replyToId;

    /*@ManyToOne
    @JoinColumn(name = "post_id")
    private Post postId;
    // TODO: Add relation with post, waiting for the completion of the Post entity */

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "comment_reaction",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "reaction_id")
    )
    private List<CommentReaction> reactions;

    private CommentStatus status;

    public enum CommentStatus {
        ENABLED,
        DISABLED;

        public static CommentStatus fromValue(String value) {
            return DataFormatterUtils.fromValue(CommentStatus.class, value);
        }

    }
}

package com.blogify.blogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Data
@ToString
@Builder
@AllArgsConstructor
@Table(name = "post")
public class Post implements Serializable {
    @Id
    private String id;

    private String title;

    private String content;

    @OneToMany(
        mappedBy = "post",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PostReaction> postReactions;
    //TODO: remove this , just test

    @CreationTimestamp
    private Instant creationDatetime;

    private Instant lastUpdateDatetime;
}

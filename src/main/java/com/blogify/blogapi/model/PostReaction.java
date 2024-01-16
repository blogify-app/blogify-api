package com.blogify.blogapi.model;

import com.blogify.blogapi.utils.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Builder
@AllArgsConstructor
public class PostReaction implements Serializable {
    private String id;
    private String userId;
    private String postId;
    private ReactionType reactionType;
}

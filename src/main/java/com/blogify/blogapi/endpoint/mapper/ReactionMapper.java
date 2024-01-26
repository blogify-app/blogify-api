package com.blogify.blogapi.endpoint.mapper;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

import com.blogify.blogapi.endpoint.rest.model.Reaction;
import com.blogify.blogapi.endpoint.rest.model.ReactionStat;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import com.blogify.blogapi.repository.model.CommentReaction;
import com.blogify.blogapi.repository.model.PostReaction;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReactionMapper {

  public Reaction toRest(PostReaction domain) {
    return new Reaction()
        .id(domain.getId())
        .postId(domain.getPost().getId())
        .commentId(null)
        .creationDatetime(domain.getCreationDatetime())
        .type(toRest(domain.getType()))
        .userId(domain.getUser().getId());
  }

  public Reaction toRest(CommentReaction domain) {
    return new Reaction()
        .id(domain.getId())
        .postId(null)
        .commentId(domain.getComment().getId())
        .creationDatetime(domain.getCreationDatetime())
        .type(toRest(domain.getType()))
        .userId(domain.getUser().getId());
  }

  public ReactionStat toRest(com.blogify.blogapi.model.ReactionStat reactionStat) {
    return new ReactionStat().dislikes(reactionStat.getDislikes()).likes(reactionStat.getLikes());
  }

  public ReactionType toRest(com.blogify.blogapi.model.enums.ReactionType reactionType) {
    return mapEnum(
        reactionType,
        Map.of(
            com.blogify.blogapi.model.enums.ReactionType.LIKE, ReactionType.LIKE,
            com.blogify.blogapi.model.enums.ReactionType.DISLIKE, ReactionType.DISLIKE));
  }

  public com.blogify.blogapi.model.enums.ReactionType toDomain(ReactionType reactionType) {
    return mapEnum(
        reactionType,
        Map.of(
            ReactionType.LIKE, com.blogify.blogapi.model.enums.ReactionType.LIKE,
            ReactionType.DISLIKE, com.blogify.blogapi.model.enums.ReactionType.DISLIKE));
  }
}

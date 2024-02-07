package com.blogify.blogapi.endpoint.mapper;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.CommentStatus;
import com.blogify.blogapi.model.ReactionStat;
import com.blogify.blogapi.repository.model.Post;
import java.util.ArrayList;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentMapper {
  private final UserMapper userMapper;
  private final ReactionMapper reactionMapper;

  public Comment toRest(
      com.blogify.blogapi.repository.model.Comment domain, ReactionStat reactionStat) {
    return new Comment()
        .id(domain.getId())
        .user(userMapper.toRest(domain.getUser()))
        .postId(domain.getPost().getId())
        .content(domain.getContent())
        .creationDatetime(domain.getCreationDatetime())
        .replyToId(domain.getReplyToId())
        .reactions(reactionMapper.toRest(reactionStat))
        .status(toRest(domain.getStatus()));
  }

  public com.blogify.blogapi.repository.model.Comment toDomain(Comment comment, Post post) {
    return com.blogify.blogapi.repository.model.Comment.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .replyToId(comment.getReplyToId())
        .user(
            comment.getUser() == null
                ? null
                : userMapper.toDomain((comment.getUser()), new ArrayList<>()))
        .status(toDomain(comment.getStatus()))
        .post(post)
        .commentReactions(null)
        .build();
  }

  public CommentStatus toRest(com.blogify.blogapi.model.enums.CommentStatus status) {
    return mapEnum(
        status,
        Map.of(
            com.blogify.blogapi.model.enums.CommentStatus.ENABLED, CommentStatus.ENABLED,
            com.blogify.blogapi.model.enums.CommentStatus.DISABLED, CommentStatus.DISABLED));
  }

  private com.blogify.blogapi.model.enums.CommentStatus toDomain(CommentStatus status) {
    return mapEnum(
        status,
        Map.of(
            CommentStatus.ENABLED, com.blogify.blogapi.model.enums.CommentStatus.ENABLED,
            CommentStatus.DISABLED, com.blogify.blogapi.model.enums.CommentStatus.DISABLED));
  }
}

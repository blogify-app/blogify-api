package com.blogify.blogapi.endpoint.mapper;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.CommentStatus;
import com.blogify.blogapi.endpoint.rest.model.UserStatus;
import com.blogify.blogapi.model.ReactionStat;
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

  public CommentStatus toRest(com.blogify.blogapi.model.enums.CommentStatus status) {
    return mapEnum(
        status,
        Map.of(
            com.blogify.blogapi.model.enums.CommentStatus.ENABLED, CommentStatus.ENABLED,
            com.blogify.blogapi.model.enums.CommentStatus.DISABLED, CommentStatus.DISABLED));
  }

  private com.blogify.blogapi.model.enums.CommentStatus toDomain(UserStatus status) {
    return mapEnum(
        status,
        Map.of(
            CommentStatus.ENABLED, com.blogify.blogapi.model.enums.CommentStatus.ENABLED,
            CommentStatus.DISABLED, com.blogify.blogapi.model.enums.CommentStatus.DISABLED));
  }
}

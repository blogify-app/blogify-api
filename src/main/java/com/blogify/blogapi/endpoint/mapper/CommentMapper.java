package com.blogify.blogapi.endpoint.mapper;

import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.endpoint.rest.model.CommentStatus;
import com.blogify.blogapi.endpoint.rest.model.ReactionStat;
import com.blogify.blogapi.endpoint.rest.model.UserStatus;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

@Component
@AllArgsConstructor
public class CommentMapper {
    private final UserMapper userMapper;
    private final ReactionStateMapper reactionStateMapper;

    public Comment toRest(com.blogify.blogapi.repository.model.Comment domain) {
        return new Comment()
                .id(domain.getId())
                .content(domain.getContent())
                .creationDatetime(domain.getCreationDatetime())
                .status(toRest(domain.getStatus()))
                .replyToId(domain.getReplyToId())
                .user(userMapper.toRest(domain.getUser()))
                .postId(domain.getPostId())
                .reactions((ReactionStat) reactionStateMapper.toRestList(domain.getReactions()));
    }

    public CommentStatus toRest(com.blogify.blogapi.model.enums.CommentStatus status) {
        return mapEnum(
            status,
            Map.of(
                    com.blogify.blogapi.model.enums.CommentStatus.ENABLED, CommentStatus.ENABLED,
                    com.blogify.blogapi.model.enums.CommentStatus.DISABLED, CommentStatus.DISABLED
            )
        );
    }
    private com.blogify.blogapi.model.enums.CommentStatus toDomain(UserStatus status) {
        return mapEnum(
                status,
                Map.of(
                        CommentStatus.ENABLED, com.blogify.blogapi.model.enums.CommentStatus.ENABLED,
                        CommentStatus.DISABLED, com.blogify.blogapi.model.enums.CommentStatus.DISABLED
                )
        );
    }
}

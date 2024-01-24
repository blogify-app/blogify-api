package com.blogify.blogapi.endpoint.mapper;

import com.blogify.blogapi.endpoint.rest.model.Reaction;
import com.blogify.blogapi.endpoint.rest.model.ReactionType;
import java.util.Map;
import org.springframework.stereotype.Component;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

@Component
public class ReactionMapper {

    public Reaction toRest(com.blogify.blogapi.repository.model.Reaction domain) {
        return new Reaction()
                .id(domain.getId())
                .type(toRest(domain.getType()))
                .creationDatetime(domain.getCreationDatetime())
                .userId(domain.getId())
                .commentId(domain.getId())
                .postId(domain.getId());
    }

    private ReactionType toRest(com.blogify.blogapi.model.enums.ReactionType reaction) {
        return mapEnum(
                reaction,
                Map.of(
                        com.blogify.blogapi.model.enums.ReactionType.LIKE, ReactionType.LIKE,
                        com.blogify.blogapi.model.enums.ReactionType.DISLIKE, ReactionType.DISLIKE));
    }

    private com.blogify.blogapi.model.enums.ReactionType toDomain(ReactionType reaction) {
        return mapEnum(
                reaction,
                Map.of(
                        ReactionType.LIKE, com.blogify.blogapi.model.enums.ReactionType.LIKE,
                        ReactionType.DISLIKE, com.blogify.blogapi.model.enums.ReactionType.DISLIKE));
    }
}

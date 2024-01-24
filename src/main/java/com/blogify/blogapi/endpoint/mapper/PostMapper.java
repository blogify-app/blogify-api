package com.blogify.blogapi.endpoint.mapper;

import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.PostStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

@Component
public class PostMapper {
    public Post toRest(com.blogify.blogapi.repository.model.Post domain){
        return new  Post()
                .id(domain.getId())
                .thumbnailUrl(domain.getThumbnailUrl())
                .description(domain.getDescription())
                .content(domain.getContent())
                .title(domain.getTitle())
                .creationDatetime(domain.getCreationDatetime())
                .updatedAt(domain.getLastUpdateDatetime())
                .status(toRest(domain.getStatus()));
    }

    public com.blogify.blogapi.repository.model.Post toDomain(Post rest){
        return com.blogify.blogapi.repository.model.Post.builder()
                .id(rest.getId())
                .thumbnailUrl(rest.getThumbnailUrl())
                .description(rest.getDescription())
                .content(rest.getContent())
                .creationDatetime(rest.getCreationDatetime())
                .lastUpdateDatetime(rest.getUpdatedAt())
                .status(toDomain(rest.getStatus()))
                .build();
    }

    private PostStatus toRest(com.blogify.blogapi.model.enums.PostStatus postStatus){
        return mapEnum(
                postStatus,
                Map.of(
                        com.blogify.blogapi.model.enums.PostStatus.DRAFT,PostStatus.DRAFT,
                        com.blogify.blogapi.model.enums.PostStatus.DISABLED,PostStatus.DISABLED,
                        com.blogify.blogapi.model.enums.PostStatus.ARCHIVED,PostStatus.ARCHIVED
                )
        );
    }

    private com.blogify.blogapi.model.enums.PostStatus toDomain(PostStatus postStatus){
        return mapEnum(
                postStatus,
                Map.of(
                        PostStatus.DRAFT, com.blogify.blogapi.model.enums.PostStatus.DRAFT,
                        PostStatus.DISABLED, com.blogify.blogapi.model.enums.PostStatus.DISABLED,
                        PostStatus.ARCHIVED, com.blogify.blogapi.model.enums.PostStatus.ARCHIVED
                )
        );
    }
}


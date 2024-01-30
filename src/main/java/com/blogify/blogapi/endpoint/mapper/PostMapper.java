package com.blogify.blogapi.endpoint.mapper;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.PostStatus;
import com.blogify.blogapi.model.ReactionStat;
import com.blogify.blogapi.repository.model.User;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostMapper {
  private final CategoryMapper categoryMapper;
  private final ReactionMapper reactionMapper;

  public Post toRest(com.blogify.blogapi.repository.model.Post domain, ReactionStat reactionStat) {
    return new Post()
        .id(domain.getId())
        .authorId(domain.getUser().getId())
        .thumbnailUrl(domain.getThumbnailUrl())
        .description(domain.getDescription())
        .content(domain.getContent())
        .title(domain.getTitle())
        .creationDatetime(domain.getCreationDatetime())
        .updatedAt(domain.getLastUpdateDatetime())
        .status(toRest(domain.getStatus()))
        .reactions(reactionMapper.toRest(reactionStat))
        .categories(domain.getPostCategories().stream().map(categoryMapper::toRest).toList());
  }

  public Post toRest(
      String content, com.blogify.blogapi.repository.model.Post domain, ReactionStat reactionStat) {
    return new Post()
        .id(domain.getId())
        .authorId(domain.getUser().getId())
        .thumbnailUrl(domain.getThumbnailUrl())
        .description(domain.getDescription())
        .content(content)
        .title(domain.getTitle())
        .creationDatetime(domain.getCreationDatetime())
        .updatedAt(domain.getLastUpdateDatetime())
        .status(toRest(domain.getStatus()))
        .reactions(reactionMapper.toRest(reactionStat))
        .categories(domain.getPostCategories().stream().map(categoryMapper::toRest).toList());
  }

  public com.blogify.blogapi.repository.model.Post toDomain(Post rest, User user) {
    return com.blogify.blogapi.repository.model.Post.builder()
        .id(rest.getId())
        .user(user)
        .thumbnailUrl(rest.getThumbnailUrl())
        .description(rest.getDescription())
        .content(rest.getContent())
        .lastUpdateDatetime(rest.getUpdatedAt())
        .title(rest.getTitle())
        .status(toDomain(rest.getStatus()))
        .postCategories(
            rest.getCategories().isEmpty()
                ? null
                : rest.getCategories().stream().map(categoryMapper::toPostCategoryDomain).toList())
        .build();
  }

  private PostStatus toRest(com.blogify.blogapi.model.enums.PostStatus postStatus) {
    return mapEnum(
        postStatus,
        Map.of(
            com.blogify.blogapi.model.enums.PostStatus.DRAFT, PostStatus.DRAFT,
            com.blogify.blogapi.model.enums.PostStatus.DISABLED, PostStatus.DISABLED,
            com.blogify.blogapi.model.enums.PostStatus.ARCHIVED, PostStatus.ARCHIVED));
  }

  private com.blogify.blogapi.model.enums.PostStatus toDomain(PostStatus postStatus) {
    return mapEnum(
        postStatus,
        Map.of(
            PostStatus.DRAFT, com.blogify.blogapi.model.enums.PostStatus.DRAFT,
            PostStatus.DISABLED, com.blogify.blogapi.model.enums.PostStatus.DISABLED,
            PostStatus.ARCHIVED, com.blogify.blogapi.model.enums.PostStatus.ARCHIVED));
  }
}

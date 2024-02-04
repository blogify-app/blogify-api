package com.blogify.blogapi.endpoint.mapper;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

import com.blogify.blogapi.constant.FileConstant;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.PostStatus;
import com.blogify.blogapi.file.S3Service;
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
  private final S3Service s3Service;

  public Post toRest(com.blogify.blogapi.repository.model.Post domain, ReactionStat reactionStat) {
    String thumbnailKey = domain.getThumbnailKey();
    return new Post()
        .id(domain.getId())
        .authorId(domain.getUser().getId())
        .thumbnailUrl(
            thumbnailKey == null
                ? null
                : s3Service
                    .generatePresignedUrl(domain.getThumbnailKey(), FileConstant.URL_DURATION)
                    .toString())
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
    String thumbnailKey = domain.getThumbnailKey();
    return new Post()
        .id(domain.getId())
        .authorId(domain.getUser().getId())
        .thumbnailUrl(
            thumbnailKey == null
                ? null
                : s3Service
                    .generatePresignedUrl(domain.getThumbnailKey(), FileConstant.URL_DURATION)
                    .toString())
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
        .description(rest.getDescription())
        .content(rest.getContent())
        //        .lastUpdateDatetime(rest.getUpdatedAt())
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

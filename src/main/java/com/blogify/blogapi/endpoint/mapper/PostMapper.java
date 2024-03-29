package com.blogify.blogapi.endpoint.mapper;

import static com.blogify.blogapi.service.utils.EnumMapperUtils.mapEnum;

import com.blogify.blogapi.constant.FileConstant;
import com.blogify.blogapi.endpoint.mapper.utils.ToDomainMapperUtils;
import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.endpoint.rest.model.PostStatus;
import com.blogify.blogapi.file.S3Service;
import com.blogify.blogapi.model.ReactionStat;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostMapper {
  private final CategoryMapper categoryMapper;
  private final ReactionMapper reactionMapper;
  private final UserMapper userMapper;
  private final S3Service s3Service;
  private final ToDomainMapperUtils toDomainMapperUtils;

  public Post toRest(
      String content, com.blogify.blogapi.repository.model.Post domain, ReactionStat reactionStat) {
    String thumbnailKey = domain.getThumbnailKey();
    return new Post()
        .id(domain.getId())
        .author(userMapper.toRest(domain.getUser()))
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

  public com.blogify.blogapi.repository.model.Post toDomain(Post rest) {
    return com.blogify.blogapi.repository.model.Post.builder()
        .id(rest.getId())
        .user(rest.getAuthor() == null ? null : userMapper.toDomain(rest.getAuthor(), null))
        .description(rest.getDescription())
        .content(rest.getContent())
        //        .lastUpdateDatetime(rest.getUpdatedAt())
        .title(rest.getTitle())
        .status(toDomain(rest.getStatus()))
        .postCategories(
            toDomainMapperUtils.checkNullList(rest.getCategories()).stream()
                .map(categoryMapper::toPostCategoryDomain)
                .toList())
        .build();
  }

  public Post toRest(com.blogify.blogapi.repository.model.Post domain, ReactionStat reactionStat) {
    String thumbnailKey = domain.getThumbnailKey();
    String Id = com.blogify.blogapi.repository.model.Post.builder().id(domain.getId()).toString();
    if (Id == null) {
      return null;
    }
    return new Post()
        .id(domain.getId())
        .author(userMapper.toRest(domain.getUser()))
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
        .categories(
            toDomainMapperUtils.checkNullList(domain.getPostCategories()).stream()
                .map(categoryMapper::toRest)
                .toList());
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

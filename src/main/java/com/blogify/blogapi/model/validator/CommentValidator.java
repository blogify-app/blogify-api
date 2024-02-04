package com.blogify.blogapi.model.validator;

import com.blogify.blogapi.model.enums.CommentStatus;
import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.repository.model.Comment;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.utils.StringUtils;

@Component
@AllArgsConstructor
public class CommentValidator implements Consumer<Comment> {
  private final Validator validator;

  public void accept(List<Comment> comments) {
    comments.forEach(this);
  }

  @Override
  public void accept(Comment comment) {
    Set<String> violationMessages = new HashSet<>();
    if (!isNotNullAndNotBlank(comment.getContent())) {
      throw new BadRequestException("Content is mandatory. ");
    }

    if (comment.getUser() == null) {
      violationMessages.add("User is mandatory");
    } else {
      if (comment.getUser().getId() == null) {
        violationMessages.add("User ID is mandatory");
      }
    }

    if (comment.getStatus() == CommentStatus.DISABLED
        || !isValidCommentStatus(comment.getStatus())) {
      violationMessages.add("Invalid Comment Status. ");
    }

    if (!violationMessages.isEmpty()) {
      String formattedViolationMessages =
          violationMessages.stream().map(String::toString).collect(Collectors.joining(". "));
      throw new BadRequestException(formattedViolationMessages);
    }
  }

  private boolean isValidCommentStatus(CommentStatus status) {
    return CommentStatus.ENABLED.equals(status);
  }

  private boolean isNotNullAndNotBlank(String content) {
    return StringUtils.isNotBlank(content);
  }
}

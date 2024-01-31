package com.blogify.blogapi.model.validator;

import com.blogify.blogapi.model.enums.CommentStatus;
import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.repository.model.Comment;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentValidator implements Consumer<Comment> {
    private final Validator validator;

    public void accept(List<Comment> comments) {
        comments.forEach(this);
    }

    @Override
    public void accept(Comment comment) {
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);

        if (!isNotNullAndNotBlank(comment.getContent())) {
            throw new BadRequestException("Content is mandatory. ");
        }

        if (comment.getUser() == null) {
            throw new BadRequestException("User is mandatory");
        }

        if (comment.getPost() == null) {
            throw new BadRequestException("Post is mandatory. ");
        }

        if (comment.getStatus() == CommentStatus.DISABLED || !isValidCommentStatus(comment.getStatus())) {
            throw new BadRequestException("Invalid Comment Status. ");
        }

        if (!violations.isEmpty()) {
            String constraintMessages =
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(". "));
            throw new BadRequestException(constraintMessages);
        }
    }

    private boolean isValidCommentStatus(CommentStatus status) {
        return CommentStatus.ENABLED.equals(status);
    }

    private boolean isNotNullAndNotBlank(String content) {
        return true;
    }
}

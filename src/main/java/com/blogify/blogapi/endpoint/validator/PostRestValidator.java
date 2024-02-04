package com.blogify.blogapi.endpoint.validator;

import com.blogify.blogapi.endpoint.rest.model.Post;
import com.blogify.blogapi.model.exception.BadRequestException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostRestValidator implements Consumer<Post> {
  private final Validator validator;

  public void accept(List<Post> posts) {
    posts.forEach(this::accept);
  }

  @Override
  public void accept(Post post) {
    Set<String> violationMessages = new HashSet<>();
    if (post.getAuthorId() == null) {
      violationMessages.add("Author_id is mandatory");
    }
    if (!violationMessages.isEmpty()) {
      String formattedViolationMessages =
          violationMessages.stream().map(String::toString).collect(Collectors.joining(". "));
      throw new BadRequestException(formattedViolationMessages);
    }
  }
}

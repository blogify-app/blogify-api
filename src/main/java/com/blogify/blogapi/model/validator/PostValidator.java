package com.blogify.blogapi.model.validator;

import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.repository.model.Post;
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
public class PostValidator implements Consumer<Post> {
  private final Validator validator;

  public void accept(List<Post> posts) {
    posts.forEach(this::accept);
  }

  @Override
  public void accept(Post post) {
    Set<String> violationMessages = new HashSet<>();
    if (post.getId() == null) {
      violationMessages.add("Post_id is mandatory");
    }
    if (post.getUser() == null) {
      violationMessages.add("User is mandatory");
    }
    if (!violationMessages.isEmpty()) {
      String formattedViolationMessages =
          violationMessages.stream().map(String::toString).collect(Collectors.joining(". "));
      throw new BadRequestException(formattedViolationMessages);
    }
  }
}

package com.blogify.blogapi.model.validator;

import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.model.exception.BadRequestException;
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
public class UserValidator implements Consumer<User> {
  private final Validator validator;

  public void accept(List<User> users) {
    users.forEach(this);
  }

  @Override
  public void accept(User user) {
    Set<ConstraintViolation<User>> violations = validator.validate(user);
    if (user.getId() == null) {
      throw new BadRequestException("User_id is mandatory");
    }
    if (!violations.isEmpty()) {
      String constraintMessages =
          violations.stream()
              .map(ConstraintViolation::getMessage)
              .collect(Collectors.joining(". "));
      throw new BadRequestException(constraintMessages);
    }
  }
}

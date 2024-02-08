package com.blogify.blogapi.model.validator;

import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.repository.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserValidator implements Consumer<User> {
  public void accept(List<User> users) {
    users.forEach(this);
  }

  @Override
  public void accept(User user) {
    Set<String> violationMessages = new HashSet<>();

    if (user.getId() == null) {
      violationMessages.add("User ID is mandatory");
    }
    if (isEmptyOrNull(user.getFirstname())) {
      violationMessages.add("First name is mandatory");
    }
    if (isEmptyOrNull(user.getLastname())) {
      violationMessages.add("Last name is mandatory");
    }
    if (isEmptyOrNull(user.getMail())) {
      violationMessages.add("Email is mandatory");
    } else if (!isValidEmail(user.getMail())) {
      violationMessages.add("Email format is invalid");
    }
    if (user.getBirthdate() == null) {
      violationMessages.add("Birthdate is mandatory");
    }
    if (!violationMessages.isEmpty()) {
      String formattedViolationMessages =
          violationMessages.stream().map(String::toString).collect(Collectors.joining(". "));
      throw new BadRequestException(formattedViolationMessages);
    }
  }

  private boolean isEmptyOrNull(String value) {
    return value == null || value.trim().isEmpty();
  }

  private boolean isValidEmail(String email) {
    String emailRegex =
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    Pattern pattern = Pattern.compile(emailRegex);
    return pattern.matcher(email).matches();
  }
}

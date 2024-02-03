package com.blogify.blogapi.model.validator;

import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.repository.model.Category;
import com.blogify.blogapi.repository.model.User;

import java.util.HashSet;
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
public class CategoryValidator implements Consumer<Category> {

    public void accept(List<Category> categories) {
        categories.forEach(this::accept);
    }

    @Override
    public void accept(Category category) {
        Set<String> violationMessages = new HashSet<>();

        if (category.getId() == null) {
            violationMessages.add("Category_id is mandatory");
        }
        if (!violationMessages.isEmpty()) {
            String formattedViolationMessages = violationMessages.stream()
                    .map(String::toString)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(formattedViolationMessages);
        }
    }
}

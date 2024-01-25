package com.blogify.blogapi.service;

import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.repository.UserRepository;
import com.blogify.blogapi.repository.model.User;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository repository;

  public List<User> findAll() {
    return repository.findAll();
  }

  public List<User> findAllByName(String name, PageFromOne page, BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
    return repository.getUserByName(name, pageable);
  }

  public User getUserByFirebaseIdAndEmail(String firebaseId, String email) {
    return repository
        .findByFirebaseIdAndMail(firebaseId, email)
        .orElseThrow(() -> new NotFoundException("User.email=" + email + " is not found."));
  }

  public Boolean checkUserOfCategory(
          String userId, String categoryId) {
    User user = repository.getReferenceById(userId);
    return user.getUserCategories().stream().anyMatch(userCategory -> userCategory.getCategory().getId().equals(categoryId));
  }

  @Transactional
  public User save(User toSave) {
    return repository.save(toSave);
  }
}

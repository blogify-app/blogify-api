package com.blogify.blogapi.service;

import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.model.validator.UserValidator;
import com.blogify.blogapi.repository.UserRepository;
import com.blogify.blogapi.repository.model.User;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository repository;
  private UserValidator userValidator;

  public List<User> findAllByName(String name, PageFromOne page, BoundedPageSize pageSize) {
    Pageable pageable = PageRequest.of(page.getValue() - 1, pageSize.getValue());
    return repository.getUserByName(name, pageable);
  }

  public User getUserByFirebaseIdAndEmail(String firebaseId, String email) {
    return repository
        .findByFirebaseIdAndMail(firebaseId, email)
        .orElseThrow(() -> new NotFoundException("User.email=" + email + " is not found."));
  }

  @Transactional
  public User save(User toSave) {
    return repository.save(toSave);
  }

  @Transactional
  public User saveSignUpUser(User toSave) {
    if (repository.findByMail(toSave.getMail()).isPresent()) {
      throw new BadRequestException("User already exists");
    }
    return repository.save(toSave);
  }

  @Transactional
  public User crupdateUser(User user, String userId) {
    userValidator.accept(user);
    Optional<User> userOptional = repository.findById(userId);
    if (userOptional.isPresent()) {
      User userFromDomain = userOptional.get();
      user.setCreationDatetime(userFromDomain.getCreationDatetime());
      user.setRole(userFromDomain.getRole());
      user.setPhotoKey(userFromDomain.getPhotoKey());
      user.setProfileBannerKey(userFromDomain.getProfileBannerKey());
    }
    return repository.save(user);
  }

  public User findById(String id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
  }
}

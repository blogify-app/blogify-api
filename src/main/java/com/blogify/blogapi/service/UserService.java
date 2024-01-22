package com.blogify.blogapi.service;

import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.repository.UserRepository;
import com.blogify.blogapi.repository.model.User;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository repository;

  public List<User> findAll() {
    return repository.findAll();
  }

  public User getUserbyFirebaseIdAndEmail(String firebaseId, String email) {
    return repository
        .findByFirebaseIdAndMail(firebaseId, email)
        .orElseThrow(() -> new NotFoundException("User.email=" + email + " is not found."));
  }

  @Transactional
  public User save(User toSave) {
    return repository.save(toSave);
  }
}

package com.blogify.blogapi.service;

import com.blogify.blogapi.repository.UserRepository;
import com.blogify.blogapi.repository.model.User;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Transactional
  public User save(User toSave) {
    return userRepository.save(toSave);
  }
}

package com.blogify.blogapi.service;

import com.blogify.blogapi.model.User;
import com.blogify.blogapi.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public User save(User toSave) {
    return userRepository.save(toSave);
  }
}

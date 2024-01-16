package com.blogify.blogapi.service.event;

import com.blogify.blogapi.repository.UserRepository;
import com.blogify.blogapi.repository.model.User;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public List<User> getAll() {
    return userRepository.findAll();
  }
}

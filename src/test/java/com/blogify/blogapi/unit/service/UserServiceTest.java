package com.blogify.blogapi.unit.service;

import static com.blogify.blogapi.utils.TestUtils.ignoreIdAnCreationDatetime;
import static com.blogify.blogapi.utils.TestUtils.user;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.UserService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends FacadeIT {
  @Autowired UserService subject;

  @Test
  void create_user_ok() {
    User actual = subject.save(user());

    assertEquals(ignoreIdAnCreationDatetime(user()), ignoreIdAnCreationDatetime(actual));
  }

  @Test
  void read_all_user() {
    User created = subject.save(user());
    List<User> actuals = subject.findAll();
    var actual = actuals.get(0);

    assertEquals(1, actuals.size());
    assertEquals(created.getId(), actual.getId());
    assertEquals(created.getFirstname(), actual.getFirstname());
    assertEquals(created.getLastname(), actual.getLastname());
    assertEquals(created.getMail(), actual.getMail());
  }
}

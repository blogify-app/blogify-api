package com.blogify.blogapi.unit.service;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.model.User;
import com.blogify.blogapi.service.firebase.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.UUID.randomUUID;

public class FirebaseServiceTest extends FacadeIT {
  @Autowired
  FirebaseService subject;

  //Uncomment when required
  /*@Test
  void create_user_ok(){
    String password = "dummy-password";

    User actual = subject.createUser(user(), password);

    assertEquals(ignoreId(user()), ignoreId(actual));
  }*/

  User user() {
    return User.builder()
        .id(randomUUID().toString())
        .mail("dummy@gmail.com")
        .firstname("John")
        .lastname("Doe")
        .build();
  }

  private User ignoreId(User user) {
    return user.toBuilder()
        .id(null)
        .build();
  }
}

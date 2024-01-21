package com.blogify.blogapi.unit.service;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.service.firebase.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;

public class FirebaseServiceTest extends FacadeIT {
  @Autowired FirebaseService subject;

  // Uncomment when required
  /*@Test
  void create_user_ok(){
    String password = "dummy-password";

    User actual = subject.createUser(user(), password);

    assertEquals(ignoreId(user()), ignoreId(actual));
  }*/

}

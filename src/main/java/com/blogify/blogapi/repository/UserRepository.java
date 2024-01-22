package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByFirebaseIdAndMail(String firebaseId, String email);
}

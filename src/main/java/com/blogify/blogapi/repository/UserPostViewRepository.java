package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.UserPostView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostViewRepository extends JpaRepository<UserPostView, String> {

  @Query("SELECT u FROM UserPostView u WHERE u.isUpload = false")
  List<UserPostView> findNotUpload();
}

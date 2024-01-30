package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.UserCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, String> {
  List<UserCategory> findUserCategoryByUser_Id(String userId);
}

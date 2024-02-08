package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.UserCategoryPoint;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCategoryPointRepository extends JpaRepository<UserCategoryPoint, String> {
  Optional<UserCategoryPoint> findByCategory_IdAndUser_Id(String categoryId, String userId);

  List<UserCategoryPoint> findByUser_Id(String userId);
}

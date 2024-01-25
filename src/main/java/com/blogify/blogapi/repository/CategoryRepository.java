package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
  List<Category> findAllByNameContainingIgnoreCase(String name);
}

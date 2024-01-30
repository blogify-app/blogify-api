package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.Post;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
  List<Post> findByUserId(String userId, Pageable pageable);
}

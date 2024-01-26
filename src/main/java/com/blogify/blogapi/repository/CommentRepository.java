package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.Comment;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
  List<Comment> findByPostIdOrderByCreationDatetimeDesc(String postId, Pageable pageable);
}

package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.Comment;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
  List<Comment> findByPostIdOrderByCreationDatetimeDesc(String postId, Pageable pageable);
  Optional<Comment> findByPostIdAndId(String postId,String commentId);
}

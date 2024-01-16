package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // List<Comment> findByPostId(Long postId);
    // Optional<Comment> findByPostIdAndId(Long postId, Long commentId);
}
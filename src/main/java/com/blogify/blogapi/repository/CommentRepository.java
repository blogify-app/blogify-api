package com.blogify.blogapi.repository;

import com.blogify.blogapi.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // TODO: Implement custom queries for handling Post-related operations

    // List<Comment> findByPostId(Long postId);
    // TODO: Retrieve comments by Post ID

    // Optional<Comment> findByPostIdAndId(Long postId, Long commentId);
    // TODO: Retrieve a comment by Post ID and Comment ID
}
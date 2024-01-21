package com.blogify.blogapi.repository;

import com.blogify.blogapi.model.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    Optional<Object> findByIdAndPostId(String commentId, String postId);
    // TODO: Implement custom queries for handling Post-related operations

     List<Comment> findByPostId(String postId);
    // TODO: Retrieve comments by Post ID

     List<Comment> findByPostIdAndId(String postId, String commentId);
    // TODO: Retrieve a comment by Post ID and Comment ID
}
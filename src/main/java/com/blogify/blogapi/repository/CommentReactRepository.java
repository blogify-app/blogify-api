package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.CommentReact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReactRepository extends JpaRepository<CommentReact, Long> {
}

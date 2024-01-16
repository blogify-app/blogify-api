package com.blogify.blogapi.repository;

import com.blogify.blogapi.model.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction,String> {
}

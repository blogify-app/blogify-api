package com.blogify.blogapi.repository;

import com.blogify.blogapi.repository.model.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction,String> {
}

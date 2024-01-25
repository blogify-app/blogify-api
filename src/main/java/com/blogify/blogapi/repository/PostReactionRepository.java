package com.blogify.blogapi.repository;

import com.blogify.blogapi.model.enums.ReactionType;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.PostReaction;
import com.blogify.blogapi.repository.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, String> {
  List<PostReaction> findAllByPostAndType(Post post, ReactionType type);

  @Query("SELECT count(p) FROM PostReaction p WHERE p.post.id = :post_id and p.type = :type")
  Long sumOfPropertyByPostAndType(
      @Param("post_id") String post_id, @Param("type") ReactionType type);

  List<PostReaction> findAllByPostAndUser(Post post, User user);
}

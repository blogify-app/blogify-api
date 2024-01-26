package com.blogify.blogapi.repository;

import com.blogify.blogapi.model.enums.ReactionType;
import com.blogify.blogapi.repository.model.Comment;
import com.blogify.blogapi.repository.model.CommentReaction;
import com.blogify.blogapi.repository.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, String> {
  @Query(
      "SELECT count(c) FROM CommentReaction c WHERE c.comment.id = :comment_id and c.type = :type")
  Long sumOfPropertyByPostAndType(
      @Param("comment_id") String comment_id, @Param("type") ReactionType type);

  List<CommentReaction> findAllByCommentAndUser(Comment comment, User user);
}

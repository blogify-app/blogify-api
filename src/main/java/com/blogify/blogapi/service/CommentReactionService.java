package com.blogify.blogapi.service;

import com.blogify.blogapi.model.ReactionStat;
import com.blogify.blogapi.model.enums.ReactionType;
import com.blogify.blogapi.repository.CommentReactionRepository;
import com.blogify.blogapi.repository.model.Comment;
import com.blogify.blogapi.repository.model.CommentReaction;
import com.blogify.blogapi.repository.model.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentReactionService {
  private final CommentReactionRepository commentReactionRepository;

  public ReactionStat getReactionStat(String commentId) {
    Long dislikeNumber =
        commentReactionRepository.sumOfPropertyByPostAndType(commentId, ReactionType.DISLIKE);
    Long likeNumber =
        commentReactionRepository.sumOfPropertyByPostAndType(commentId, ReactionType.LIKE);
    return ReactionStat.builder()
        .likes(BigDecimal.valueOf(likeNumber))
        .dislikes(BigDecimal.valueOf(dislikeNumber))
        .build();
  }

  public CommentReaction reactAComment(Comment comment, ReactionType reactionType, User user) {
    List<CommentReaction> commentReactions =
        commentReactionRepository.findAllByCommentAndUser(comment, user);
    CommentReaction commentReaction = new CommentReaction();
    commentReaction.setComment(comment);
    commentReaction.setId(UUID.randomUUID().toString());
    commentReaction.setUser(user);
    commentReaction.setType(reactionType);
    if (!commentReactions.isEmpty()) {
      commentReactionRepository.deleteAll(commentReactions);
    }
    return commentReactionRepository.save(commentReaction);
  }
}

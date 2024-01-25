package com.blogify.blogapi.service;

import com.blogify.blogapi.model.ReactionStat;
import com.blogify.blogapi.model.enums.ReactionType;
import com.blogify.blogapi.repository.PostReactionRepository;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.PostReaction;
import com.blogify.blogapi.repository.model.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostReactionService {
  private final PostReactionRepository postReactionRepository;

  public ReactionStat getReactionStat(String pstId) {

    Long dislikeNumber =
        postReactionRepository.sumOfPropertyByPostAndType(pstId, ReactionType.DISLIKE);
    Long likeNumber = postReactionRepository.sumOfPropertyByPostAndType(pstId, ReactionType.LIKE);
    return ReactionStat.builder()
        .likes(BigDecimal.valueOf(likeNumber))
        .dislikes(BigDecimal.valueOf(dislikeNumber))
        .build();
  }

  public PostReaction reactAPost(Post post, ReactionType reactionType, User user) {
    List<PostReaction> postReactions = postReactionRepository.findAllByPost(post);
    PostReaction postReaction = new PostReaction();
    postReaction.setPost(post);
    postReaction.setId(UUID.randomUUID().toString());
    postReaction.setUser(user);
    postReaction.setType(reactionType);
    if (!postReactions.isEmpty()) {
      postReactionRepository.deleteAll(postReactions);
    }
    return postReactionRepository.save(postReaction);
  }
}

package com.blogify.blogapi.service;

import com.blogify.blogapi.endpoint.mapper.CommentMapper;
import com.blogify.blogapi.repository.ReactionStateRepository;
import com.blogify.blogapi.repository.CommentRepository;
import com.blogify.blogapi.repository.model.Comment;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReactionStateRepository commentReactionRepository;
    private CommentMapper commentMapper;

    /*public Reaction reactToComment(String postId, String commentId, Reaction.ReactionType reactionType) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setComment(comment);
        commentReaction.setReactionType(reactionType);
        commentReactionRepository.save(commentReaction);

        return new CommentReaction(commentReaction.getId(), reactionType);
    }*/
    // TODO: React to a comment by identifier. Waiting for the completion of the Post.

    public List<com.blogify.blogapi.endpoint.rest.model.Comment> getComments() {
        List<com.blogify.blogapi.repository.model.Comment> repositoryComments = commentRepository.findAll();
        return repositoryComments.stream()
                .map(commentMapper::toRest)
                .collect(Collectors.toList());
    }

    // TODO: Get comments of identified post.

    /*public List<Comment> createOrUpdateComment(String postId, List<Comment> comments) {
        // Vous devez implémenter PostRepository pour obtenir le post par ID
        for (Comment comment : comments) {
            comment.setPost(postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException(postId)));
            commentRepository.save(comment);
        }

        return commentRepository.findByPostId(postId);
    }*/

    // TODO: Create or update comment

    /*public Comment getCommentById(String postId, String commentId) {
        return commentRepository.findByPost_IdAndId(postId, commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }*/

    // TODO: Get comment by identifier
}

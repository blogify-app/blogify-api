package com.blogify.blogapi.service;

import com.blogify.blogapi.model.CommentReaction;
import com.blogify.blogapi.model.Reaction;
import com.blogify.blogapi.repository.CommentReactionRepository;
import com.blogify.blogapi.repository.CommentRepository;
import com.blogify.blogapi.model.Comment;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentReactionRepository commentReactionRepository;

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

    public List<Comment> getComments(String postId) {
        return commentRepository.findByPostId(postId);
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

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}

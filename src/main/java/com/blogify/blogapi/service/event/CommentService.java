package com.blogify.blogapi.service.event;

import com.blogify.blogapi.repository.CommentRepository;
import com.blogify.blogapi.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    /* public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }*/
    // TODO: Implement a method to retrieve a list of comments by Post ID. Waiting for the completion of the Post.

    /* public Comment getCommentByPostIdAndCommentId(Long postId, Long commentId) {
        return commentRepository.findByPostIdAndId(postId, commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }*/

    // TODO: Implement a method to retrieve a specific comment by Post ID and Comment ID.

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}

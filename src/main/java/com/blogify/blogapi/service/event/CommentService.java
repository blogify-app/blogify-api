package com.blogify.blogapi.service.event;

import com.blogify.blogapi.repository.CommentRepository;
import com.blogify.blogapi.repository.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    /* public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }*/

    /* public Comment getCommentByPostIdAndCommentId(Long postId, Long commentId) {
        return commentRepository.findByPostIdAndId(postId, commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }*/

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}

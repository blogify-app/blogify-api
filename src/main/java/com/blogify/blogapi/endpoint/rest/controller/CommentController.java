package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.rest.model.Comment;
import com.blogify.blogapi.repository.model.Reaction;
import com.blogify.blogapi.service.CommentService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    @GetMapping("/comments")
    public List<Comment> getComments() {
        return commentService.getComments();
    }

    /*@PostMapping("/{post_id}/comments")
    public ResponseEntity<List<Comment>> createOrUpdateComment(@PathVariable("post_id") String postId, @RequestBody List<Comment> comments) {
        List<Comment> updatedComments = commentService.createOrUpdateComment(postId, comments);
        return ResponseEntity.ok(updatedComments);
    }

    @PostMapping("/{post_id}/comments/{comment_id}/react")
    public ResponseEntity<Reaction> reactToComment(
            @PathVariable("post_id") String postId,
            @PathVariable("comment_id") String commentId,
            @RequestBody Reaction.ReactionType reactionType) {
        Reaction reaction = commentService.reactToComment(postId, commentId, reactionType);
        return ResponseEntity.ok(reaction);
    }*/
}


package com.blogify.blogapi.unit;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.model.enums.CommentStatus;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.model.validator.CommentValidator;
import com.blogify.blogapi.repository.CommentRepository;
import com.blogify.blogapi.repository.model.Comment;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.repository.model.User;
import com.blogify.blogapi.service.CommentService;
import java.util.ArrayList;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentUnitTest extends FacadeIT {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @Test
    void getByIdWithExistingComment() {
        // Given
        String commentId = "commentId";
        String postId = "postId";
        Comment expectedComment = createTestComment(commentId, postId);

        Comment actualComment = commentService.getBYId(commentId, postId);

        assertEquals(expectedComment, actualComment);
    }

    // Helper method to create a test comment
    private Comment createTestComment(String commentId, String postId) {
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPost(new Post(postId)); // Assuming there's a constructor for Post that accepts postId
        comment.setContent("Test content");
        comment.setUser(new User("testUserId", "Test User")); // Assuming there's a constructor for User that accepts userId and userName
        comment.setReplyToId("replyToTestId");
        comment.setCommentReactions(new ArrayList<>()); // Assuming you want to initialize an empty list for commentReactions
        comment.setStatus(CommentStatus.ENABLED); // Assuming ENABLED is a valid status
        comment.setDeleted(false);
        return comment;
    }

    @Test
    void getByIdNoExistingComment() {
        String commentId = "commentId";
        String postId = "postId";
        assertThrows(NotFoundException.class, () -> commentService.getBYId(commentId, postId));
    }

    @Test
    void crupdateByIdWithExistingComment() {
        // Given
        String postId = "post1_id";
        String commentId = "comment3_id";
        // Comment existingComment = createTestComment(commentId, postId);
        Comment updatedComment = createTestComment(commentId, postId); // Assume updated fields here
        updatedComment.setContent("Updated content");

        Comment result = commentService.crupdateById(postId, commentId, updatedComment);

        assertEquals(updatedComment, result);

        assertNotNull(result.getCreationDatetime());
        assertEquals("Updated content", result.getContent());
    }

    @Test
    void crupdateByIdNoExistingComment() {
        String postId = "postId";
        String commentId = "commentId";
        Comment updatedComment = createTestComment(commentId, postId);

        assertThrows(NotFoundException.class, () -> commentService.crupdateById(postId, commentId, updatedComment));
    }

    @Test
    void deleteByIdWithExistingComment() {
        String postId = "postId";
        String commentId = "commentId";
        Comment existingComment = createTestComment(commentId, postId);

        Comment result = commentService.deleteById(commentId, postId);

        // Then
        assertEquals(existingComment, result);
        verify(commentRepository).delete(existingComment);
    }
    @Test
    void deleteByIdNoExistingComment() {
        String postId = "postId";
        String commentId = "commentId";

        assertThrows(NotFoundException.class, () -> commentService.deleteById(commentId, postId));
    }
}
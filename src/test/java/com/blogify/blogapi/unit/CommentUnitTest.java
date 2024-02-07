package com.blogify.blogapi.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.blogify.blogapi.conf.FacadeIT;
import com.blogify.blogapi.model.BoundedPageSize;
import com.blogify.blogapi.model.PageFromOne;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.model.validator.CommentValidator;
import com.blogify.blogapi.repository.CommentRepository;
import com.blogify.blogapi.repository.model.Comment;
import com.blogify.blogapi.repository.model.Post;
import com.blogify.blogapi.service.CommentService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class CommentUnitTest extends FacadeIT {

  @Mock private CommentRepository commentRepository;

  @Mock private CommentValidator commentValidator;

  @InjectMocks private CommentService commentService;

  @Test
  public void testGetById() {
    Comment comment = new Comment();
    Post post = new Post();
    post.setId("postId");

    comment.setPost(post);
    comment.setId("commentId");

    when(commentRepository.findByIdAndPost_Id(eq("commentId"), eq("postId")))
        .thenReturn(Optional.of(comment));

    Comment result = commentService.getBYId("commentId", "postId");

    assertEquals(comment, result);
  }

  @Test
  public void testGetById_NotFound() {
    when(commentRepository.findByIdAndPost_Id(any(), any())).thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class, () -> commentService.getBYId("notExistingCommentId", "postId"));
  }

  @Test
  public void testFindByPostId() {
    List<Comment> comments = new ArrayList<>();
    comments.add(new Comment());
    comments.add(new Comment());

    when(commentRepository.findByPostIdOrderByCreationDatetimeDesc(
            eq("postId"), any(PageRequest.class)))
        .thenReturn(comments);

    List<Comment> result =
        commentService.findByPostId("postId", new PageFromOne(1), new BoundedPageSize(10));

    assertEquals(comments.size(), result.size());
    assertEquals(comments.get(0), result.get(0));
    assertEquals(comments.get(1), result.get(1));
  }

  @Test
  public void testFindByIdAndPostId_WhenCommentExists() {
    Comment comment = new Comment();
    comment.setId("1");
    comment.setPost(new Post());

    when(commentRepository.findByIdAndPost_Id("1", "postId")).thenReturn(Optional.of(comment));

    Comment result = commentService.findByIdAndPostId("1", "postId");

    assertEquals(comment, result);
  }

  @Test
  public void testFindByIdAndPostId_WhenCommentDoesNotExist() {
    when(commentRepository.findByIdAndPost_Id("1", "postId")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> commentService.findByIdAndPostId("1", "postId"));
  }

  @Test
  public void testCommentCrupdateById() {
    Comment existingComment = new Comment();
    existingComment.setId("1");
    existingComment.setContent("Existing content");
    existingComment.setCreationDatetime(Instant.now());

    when(commentRepository.findByIdAndPost_Id(existingComment.getId(), "postId"))
        .thenReturn(Optional.of(existingComment));
    when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(existingComment);

    Mockito.doNothing().when(commentValidator).accept(existingComment);

    Comment updatedComment =
        commentService.crupdateById("postId", existingComment.getId(), existingComment);

    assertNotNull(updatedComment);

    updatedComment.setContent("Updated content");
    Mockito.verify(commentRepository).save(Mockito.any(Comment.class));

    Comment result = commentService.crupdateById("postId", updatedComment.getId(), updatedComment);

    verify(commentRepository, times(2)).findByIdAndPost_Id(existingComment.getId(), "postId");
    verify(commentRepository, times(2)).save(Mockito.any(Comment.class));
    assertEquals(updatedComment.getContent(), result.getContent());
    assertEquals(existingComment.getCreationDatetime(), result.getCreationDatetime());
  }

  @Test
  public void testDeleteById() {
    Comment commentToDelete = new Comment();
    Post post = new Post();
    post.setId("postId");
    commentToDelete.setId("commentId");
    commentToDelete.setPost(post);

    when(commentRepository.findByIdAndPost_Id("commentId", "postId"))
        .thenReturn(Optional.of(commentToDelete));

    Comment deletedComment = commentService.deleteById("commentId", "postId");

    assertEquals(commentToDelete, deletedComment);
    verify(commentRepository, times(1)).delete(commentToDelete);
  }
}

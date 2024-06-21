package com.example.freetours.test;

import com.example.freetours.controller.CommentController;
import com.example.freetours.model.Comment;
import com.example.freetours.model.Tour;
import com.example.freetours.model.User;
import com.example.freetours.service.CommentService;
import com.example.freetours.service.TourService;
import com.example.freetours.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private UserService userService;

    @Mock
    private TourService tourService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentController commentController;

    private Comment comment;
    private User user;
    private Tour tour;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        tour = new Tour();
        tour.setId(1L);
        tour.setName("Test Tour");

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        comment.setUser(user);
        comment.setTour(tour);
    }

    @Test
    void testGetAllComments() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentService.getAllComments()).thenReturn(comments);

        List<Comment> result = commentController.getAllComments();
        assertEquals(1, result.size());
        assertEquals("Test Comment", result.get(0).getText());
    }

    @Test
    void testGetCommentById() {
        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

        ResponseEntity<Comment> response = commentController.getCommentById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Comment", response.getBody().getText());
    }

    @Test
    void testGetCommentByIdNotFound() {
        when(commentService.getCommentById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Comment> response = commentController.getCommentById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCreateComment() {
        when(authentication.getName()).thenReturn("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(tourService.getTourById(1L)).thenReturn(Optional.of(tour));
        when(commentService.saveComment(any(Comment.class))).thenReturn(comment);

        ResponseEntity<?> response = commentController.createComment(comment, authentication);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Comment", ((Comment) response.getBody()).getText());
    }

    @Test
    void testCreateCommentInvalidUserOrTour() {
        when(authentication.getName()).thenReturn("testuser");
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.empty());

        ResponseEntity<?> response = commentController.createComment(comment, authentication);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid user or tour ID", response.getBody());
    }

    @Test
    void testUpdateComment() {
        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));
        when(commentService.saveComment(any(Comment.class))).thenReturn(comment);

        comment.setText("Updated Comment");
        ResponseEntity<Comment> response = commentController.updateComment(1L, comment);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Comment", response.getBody().getText());
    }

    @Test
    void testUpdateCommentNotFound() {
        when(commentService.getCommentById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Comment> response = commentController.updateComment(1L, comment);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteComment() {
        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));
        doNothing().when(commentService).deleteComment(1L);

        ResponseEntity<Void> response = commentController.deleteComment(1L);
        assertEquals(204, response.getStatusCodeValue());
        verify(commentService, times(1)).deleteComment(1L);
    }

    @Test
    void testDeleteCommentNotFound() {
        when(commentService.getCommentById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = commentController.deleteComment(1L);
        assertEquals(404, response.getStatusCodeValue());
    }
}

package com.example.freetours.test;

import com.example.freetours.model.Comment;
import com.example.freetours.repository.CommentRepository;
import com.example.freetours.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setId(1L);
        comment.setText("This is a test comment");
    }

    @Test
    void testGetAllComments() {
        List<Comment> comments = Arrays.asList(comment);
        when(commentRepository.findAll()).thenReturn(comments);

        List<Comment> result = commentService.getAllComments();
        assertEquals(1, result.size());
        assertEquals("This is a test comment", result.get(0).getText());
    }

    @Test
    void testGetCommentById() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Optional<Comment> result = commentService.getCommentById(1L);
        assertTrue(result.isPresent());
        assertEquals("This is a test comment", result.get().getText());
    }

    @Test
    void testSaveComment() {
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.saveComment(comment);
        assertEquals("This is a test comment", result.getText());
    }

    @Test
    void testDeleteComment() {
        doNothing().when(commentRepository).deleteById(1L);

        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).deleteById(1L);
    }
}

package com.example.freetours.test;

import com.example.freetours.model.Comment;
import com.example.freetours.model.Tour;
import com.example.freetours.model.User;
import com.example.freetours.repository.CommentRepository;
import com.example.freetours.repository.TourRepository;
import com.example.freetours.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TourRepository tourRepository;

    private User user;
    private Tour tour;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");
        entityManager.persist(user);

        tour = new Tour();
        tour.setName("Test Tour");
        tour.setDescription("This is a test tour.");
        tour.setPrice(100.0);
        tour.setCity("Test City");
        entityManager.persist(tour);

        comment = new Comment();
        comment.setText("This is a test comment.");
        comment.setUser(user);
        comment.setTour(tour);
        entityManager.persist(comment);
        entityManager.flush();
    }

    @Test
    void testFindById() {
        Optional<Comment> foundComment = commentRepository.findById(comment.getId());
        assertTrue(foundComment.isPresent());
        assertEquals(comment.getText(), foundComment.get().getText());
    }

    @Test
    void testFindAll() {
        List<Comment> comments = commentRepository.findAll();
        assertEquals(1, comments.size());
        assertEquals(comment.getText(), comments.get(0).getText());
    }

    @Test
    void testSaveComment() {
        Comment newComment = new Comment();
        newComment.setText("This is another test comment.");
        newComment.setUser(user);
        newComment.setTour(tour);

        Comment savedComment = commentRepository.save(newComment);
        assertEquals("This is another test comment.", savedComment.getText());
    }

    @Test
    void testDeleteComment() {
        commentRepository.deleteById(comment.getId());
        Optional<Comment> deletedComment = commentRepository.findById(comment.getId());
        assertTrue(deletedComment.isEmpty());
    }
}

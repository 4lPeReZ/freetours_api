package com.example.freetours.controller;

import com.example.freetours.model.Comment;
import com.example.freetours.model.Tour;
import com.example.freetours.model.User;
import com.example.freetours.service.CommentService;
import com.example.freetours.service.TourService;
import com.example.freetours.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        return comment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createComment(@Valid @RequestBody Comment comment, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> user = userService.getUserByUsername(username);
        Optional<Tour> tour = tourService.getTourById(comment.getTour().getId());

        if (user.isPresent() && tour.isPresent()) {
            comment.setUser(user.get());
            comment.setTour(tour.get());
            return ResponseEntity.ok(commentService.saveComment(comment));
        } else {
            return ResponseEntity.badRequest().body("Invalid user or tour ID");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment commentDetails) {
        Optional<Comment> comment = commentService.getCommentById(id);
        if (comment.isPresent()) {
            Comment commentToUpdate = comment.get();
            commentToUpdate.setText(commentDetails.getText());
            return ResponseEntity.ok(commentService.saveComment(commentToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        if (comment.isPresent()) {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.example.freetours.controller;

import com.example.freetours.model.Review;
import com.example.freetours.model.Tour;
import com.example.freetours.model.User;
import com.example.freetours.service.ReviewService;
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
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody Review review, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> user = userService.getUserByUsername(username);
        Optional<Tour> tour = tourService.getTourById(review.getTour().getId());

        if (user.isPresent() && tour.isPresent()) {
            review.setUser(user.get());
            review.setTour(tour.get());
            return ResponseEntity.ok(reviewService.saveReview(review));
        } else {
            return ResponseEntity.badRequest().body("Invalid user or tour ID");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @Valid @RequestBody Review reviewDetails) {
        Optional<Review> review = reviewService.getReviewById(id);
        if (review.isPresent()) {
            Review reviewToUpdate = review.get();
            reviewToUpdate.setText(reviewDetails.getText());
            reviewToUpdate.setRating(reviewDetails.getRating());
            return ResponseEntity.ok(reviewService.saveReview(reviewToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        if (review.isPresent()) {
            reviewService.deleteReview(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

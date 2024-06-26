package com.example.freetours.controller;

import com.example.freetours.model.Tour;
import com.example.freetours.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    @Autowired
    private TourService tourService;

    @GetMapping
    public List<Tour> getAllTours() {
        return tourService.getAllTours();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tour> getTourById(@PathVariable Long id) {
        Optional<Tour> tour = tourService.getTourById(id);
        return tour.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tour> createTour(@Valid @RequestBody Tour tour) {
        return ResponseEntity.ok(tourService.saveTour(tour));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tour> updateTour(@PathVariable Long id, @Valid @RequestBody Tour tourDetails) {
        Optional<Tour> tour = tourService.getTourById(id);
        if (tour.isPresent()) {
            Tour tourToUpdate = tour.get();
            tourToUpdate.setName(tourDetails.getName());
            tourToUpdate.setDescription(tourDetails.getDescription());
            tourToUpdate.setPrice(tourDetails.getPrice());
            tourToUpdate.setCity(tourDetails.getCity());
            return ResponseEntity.ok(tourService.saveTour(tourToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        Optional<Tour> tour = tourService.getTourById(id);
        if (tour.isPresent()) {
            tourService.deleteTour(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

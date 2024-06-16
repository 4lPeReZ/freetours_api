// src/main/java/com/example/freetours/controller/TourController.java
package com.example.freetours.controller;

import com.example.freetours.model.Tour;
import com.example.freetours.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (tour.isPresent()) {
            return ResponseEntity.ok(tour.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Tour createTour(@RequestBody Tour tour) {
        return tourService.saveTour(tour);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tour> updateTour(@PathVariable Long id, @RequestBody Tour tourDetails) {
        Optional<Tour> tour = tourService.getTourById(id);
        if (tour.isPresent()) {
            Tour tourToUpdate = tour.get();
            tourToUpdate.setName(tourDetails.getName());
            tourToUpdate.setDescription(tourDetails.getDescription());
            tourToUpdate.setPrice(tourDetails.getPrice());
            return ResponseEntity.ok(tourService.saveTour(tourToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
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

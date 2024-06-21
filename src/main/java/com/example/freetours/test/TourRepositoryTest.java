package com.example.freetours.test;

import com.example.freetours.model.Tour;
import com.example.freetours.repository.TourRepository;
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
public class TourRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TourRepository tourRepository;

    private Tour tour;

    @BeforeEach
    void setUp() {
        tour = new Tour();
        tour.setName("Test Tour");
        tour.setDescription("This is a test tour.");
        tour.setPrice(100.0);
        tour.setCity("Test City");

        entityManager.persist(tour);
        entityManager.flush();
    }

    @Test
    void testFindById() {
        Optional<Tour> foundTour = tourRepository.findById(tour.getId());
        assertTrue(foundTour.isPresent());
        assertEquals(tour.getName(), foundTour.get().getName());
    }

    @Test
    void testFindAll() {
        List<Tour> tours = tourRepository.findAll();
        assertEquals(1, tours.size());
        assertEquals(tour.getName(), tours.get(0).getName());
    }

    @Test
    void testSaveTour() {
        Tour newTour = new Tour();
        newTour.setName("New Test Tour");
        newTour.setDescription("This is another test tour.");
        newTour.setPrice(150.0);
        newTour.setCity("New Test City");

        Tour savedTour = tourRepository.save(newTour);
        assertEquals("New Test Tour", savedTour.getName());
        assertEquals("New Test City", savedTour.getCity());
    }

    @Test
    void testDeleteTour() {
        tourRepository.deleteById(tour.getId());
        Optional<Tour> deletedTour = tourRepository.findById(tour.getId());
        assertTrue(deletedTour.isEmpty());
    }
}

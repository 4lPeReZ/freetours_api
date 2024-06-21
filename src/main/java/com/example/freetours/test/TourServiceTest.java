package com.example.freetours.test;

import com.example.freetours.model.Tour;
import com.example.freetours.repository.TourRepository;
import com.example.freetours.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    @InjectMocks
    private TourService tourService;

    @Mock
    private TourRepository tourRepository;

    private Tour tour;

    @BeforeEach
    void setUp() {
        tour = new Tour();
        tour.setId(1L);
        tour.setName("Sample Tour");
        tour.setDescription("Sample Description");
        // Set other properties as needed
    }

    @Test
    void testGetAllTours() {
        when(tourRepository.findAll()).thenReturn(List.of(tour));
        List<Tour> tours = tourService.getAllTours();
        assertNotNull(tours);
        assertEquals(1, tours.size());
        assertEquals("Sample Tour", tours.get(0).getName());
    }

    @Test
    void testGetTourById() {
        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        Optional<Tour> retrievedTour = tourService.getTourById(1L);
        assertTrue(retrievedTour.isPresent());
        assertEquals("Sample Tour", retrievedTour.get().getName());
    }

    @Test
    void testSaveTour() {
        when(tourRepository.save(any(Tour.class))).thenReturn(tour);
        Tour savedTour = tourService.saveTour(tour);
        assertNotNull(savedTour);
        assertEquals("Sample Tour", savedTour.getName());
    }

    @Test
    void testDeleteTour() {
        doNothing().when(tourRepository).deleteById(1L);
        tourService.deleteTour(1L);
        verify(tourRepository, times(1)).deleteById(1L);
    }
}

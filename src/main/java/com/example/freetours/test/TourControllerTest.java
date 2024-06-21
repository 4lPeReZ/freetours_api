package com.example.freetours.test;

import com.example.freetours.controller.TourController;
import com.example.freetours.model.Tour;
import com.example.freetours.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourControllerTest {

    @Mock
    private TourService tourService;

    @InjectMocks
    private TourController tourController;

    private Tour tour;

    @BeforeEach
    void setUp() {
        tour = new Tour();
        tour.setId(1L);
        tour.setName("Test Tour");
        tour.setDescription("Test Description");
        tour.setPrice(100.0);
        tour.setCity("Test City");
    }

    @Test
    void testGetAllTours() {
        List<Tour> tours = Arrays.asList(tour);
        when(tourService.getAllTours()).thenReturn(tours);

        List<Tour> result = tourController.getAllTours();
        assertEquals(1, result.size());
        assertEquals("Test Tour", result.get(0).getName());
    }

    @Test
    void testGetTourById() {
        when(tourService.getTourById(1L)).thenReturn(Optional.of(tour));

        ResponseEntity<Tour> response = tourController.getTourById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Tour", response.getBody().getName());
    }

    @Test
    void testGetTourByIdNotFound() {
        when(tourService.getTourById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Tour> response = tourController.getTourById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateTour() {
        when(tourService.saveTour(any(Tour.class))).thenReturn(tour);

        ResponseEntity<Tour> response = tourController.createTour(tour);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Tour", response.getBody().getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateTour() {
        when(tourService.getTourById(1L)).thenReturn(Optional.of(tour));
        when(tourService.saveTour(any(Tour.class))).thenReturn(tour);

        tour.setName("Updated Tour");
        ResponseEntity<Tour> response = tourController.updateTour(1L, tour);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Tour", response.getBody().getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateTourNotFound() {
        when(tourService.getTourById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Tour> response = tourController.updateTour(1L, tour);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteTour() {
        when(tourService.getTourById(1L)).thenReturn(Optional.of(tour));
        doNothing().when(tourService).deleteTour(1L);

        ResponseEntity<Void> response = tourController.deleteTour(1L);
        assertEquals(204, response.getStatusCodeValue());
        verify(tourService, times(1)).deleteTour(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteTourNotFound() {
        when(tourService.getTourById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = tourController.deleteTour(1L);
        assertEquals(404, response.getStatusCodeValue());
    }
}

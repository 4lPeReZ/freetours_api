package com.example.freetours.test;

import com.example.freetours.model.Reservation;
import com.example.freetours.repository.ReservationRepository;
import com.example.freetours.service.ReservationService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setId(1L);
        // set other properties as needed
    }

    @Test
    void testGetAllReservations() {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservations();
        assertEquals(1, result.size());
        // assert other properties as needed
    }

    @Test
    void testGetReservationById() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById(1L);
        assertNotNull(result);
        // assert other properties as needed
    }

    @Test
    void testGetReservationByIdNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        Reservation result = reservationService.getReservationById(1L);
        assertNull(result);
    }

    @Test
    void testCreateReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.createReservation(reservation);
        assertNotNull(result);
        // assert other properties as needed
    }

    @Test
    void testUpdateReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.updateReservation(1L, reservation);
        assertNotNull(result);
        // assert other properties as needed
    }

    @Test
    void testUpdateReservationNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        Reservation result = reservationService.updateReservation(1L, reservation);
        assertNull(result);
    }

    @Test
    void testDeleteReservation() {
        doNothing().when(reservationRepository).deleteById(1L);

        reservationService.deleteReservation(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }
}

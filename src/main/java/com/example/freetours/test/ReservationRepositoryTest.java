package com.example.freetours.test;

import com.example.freetours.model.Reservation;
import com.example.freetours.model.Tour;
import com.example.freetours.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.validation.ConstraintViolationException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    private Reservation reservation;
    private User user;
    private Tour tour;

    @BeforeEach
    public void setUp() {
        // Crear un usuario y un tour antes de la prueba
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");
        entityManager.persistAndFlush(user);

        tour = new Tour();
        tour.setName("Test Tour");
        tour.setDescription("Description of the test tour");
        tour.setCity("Test City");
        tour.setPrice(100.0);
        entityManager.persistAndFlush(tour);

        // Crear una reserva con todas las propiedades requeridas
        reservation = new Reservation();
        reservation.setStatus("Confirmed");
        reservation.setDate(new Date());
        reservation.setNumPeople(4);
        reservation.setUser(user);
        reservation.setTour(tour);
    }

    @Test
    public void testPersistReservation() {
        Reservation savedReservation = entityManager.persistAndFlush(reservation);
        // Asserts
    }

    @Test
    public void testPersistInvalidReservation() {
        Reservation invalidReservation = new Reservation();  // No se configuran las propiedades requeridas
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(invalidReservation);
        });
    }
}

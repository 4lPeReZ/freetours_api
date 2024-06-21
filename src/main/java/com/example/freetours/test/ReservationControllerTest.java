package com.example.freetours.test;

import com.example.freetours.model.Reservation;
import com.example.freetours.model.Tour;
import com.example.freetours.model.User;
import com.example.freetours.service.ReservationService;
import com.example.freetours.service.TourService;
import com.example.freetours.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");
        user = userService.saveUser(user);

        Tour tour = new Tour();
        tour.setName("Test Tour");
        tour.setDescription("Test Tour Description");
        tour.setCity("Test City");
        tour.setPrice(100.0);
        tour = tourService.saveTour(tour);

        reservation = new Reservation();
        reservation.setStatus("Confirmed");
        reservation.setDate(new Date());
        reservation.setNumPeople(4);
        reservation.setUser(user);
        reservation.setTour(tour);
        reservationService.createReservation(reservation);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetAllReservations() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetReservationById() throws Exception {
        mockMvc.perform(get("/api/reservations/{id}", reservation.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateReservation() throws Exception {
        String jsonContent = String.format("{\"status\":\"Confirmed\",\"date\":\"%tF\",\"numPeople\":4,\"user\":{\"id\":%d},\"tour\":{\"id\":%d}}",
                reservation.getDate(), reservation.getUser().getId(), reservation.getTour().getId());
        mockMvc.perform(post("/api/reservations")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateReservation() throws Exception {
        String jsonContent = String.format("{\"status\":\"Updated\",\"date\":\"%tF\",\"numPeople\":4,\"user\":{\"id\":%d},\"tour\":{\"id\":%d}}",
                reservation.getDate(), reservation.getUser().getId(), reservation.getTour().getId());
        mockMvc.perform(put("/api/reservations/{id}", reservation.getId())
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteReservation() throws Exception {
        mockMvc.perform(delete("/api/reservations/{id}", reservation.getId()))
                .andExpect(status().isNoContent());
    }
}

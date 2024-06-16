// src/main/java/com/example/freetours/repository/ReservationRepository.java
package com.example.freetours.repository;

import com.example.freetours.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}

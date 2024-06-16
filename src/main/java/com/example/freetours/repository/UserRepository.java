// src/main/java/com/example/freetours/repository/UserRepository.java
package com.example.freetours.repository;

import com.example.freetours.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

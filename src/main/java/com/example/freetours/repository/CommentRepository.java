// src/main/java/com/example/freetours/repository/CommentRepository.java
package com.example.freetours.repository;

import com.example.freetours.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}

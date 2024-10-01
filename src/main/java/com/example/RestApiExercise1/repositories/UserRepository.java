package com.example.RestApiExercise1.repositories;

import com.example.RestApiExercise1.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User getById(Long id);

}

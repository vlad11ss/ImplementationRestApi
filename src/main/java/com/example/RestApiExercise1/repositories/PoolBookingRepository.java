package com.example.RestApiExercise1.repositories;

import com.example.RestApiExercise1.models.PoolBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PoolBookingRepository extends JpaRepository<PoolBooking, Long> {
    List<PoolBooking> findByDate(LocalDate date);
    List<PoolBooking> findByDateAndTime(LocalDate date, String time);
    List<PoolBooking> findByUserIdAndDateAndTime(Long userId, LocalDate date, String time);
}

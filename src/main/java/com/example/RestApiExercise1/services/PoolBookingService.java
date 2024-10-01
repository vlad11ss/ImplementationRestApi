package com.example.RestApiExercise1.services;

import com.example.RestApiExercise1.models.PoolBooking;
import com.example.RestApiExercise1.repositories.PoolBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PoolBookingService {
    private final PoolBookingRepository poolBookingRepository;

    public boolean createBooking(PoolBooking booking) {
        // Получаем все записи на указанную дату и время
        List<PoolBooking> bookingsAtThisTime = poolBookingRepository
                .findByDateAndTime(booking.getDate(), booking.getTime());

        // Подсчитываем общее количество бронирований
        int totalBookings = bookingsAtThisTime.stream().mapToInt(PoolBooking::getCount).sum();

        // Проверяем, не превышен ли лимит
        if (totalBookings + booking.getCount() > 10) {
            return false; // Лимит достигнут
        }

        // Проверяем, существует ли уже бронирование текущего пользователя
        PoolBooking existingBooking = bookingsAtThisTime.stream()
                .filter(b -> b.getUser().getId().equals(booking.getUser().getId()))
                .findFirst()
                .orElse(null);

        if (existingBooking != null) {
            // Увеличиваем количество существующего бронирования
            existingBooking.setCount(existingBooking.getCount() + booking.getCount());
            poolBookingRepository.save(existingBooking);
        } else {
            // Сохраняем новое бронирование
            booking.setOrderId("Записан");
            poolBookingRepository.save(booking);
        }

        return true;
    }

    public List<PoolBooking> getBookingsByDateAndTime(LocalDate date, String time) {
        return poolBookingRepository.findByDateAndTime(date, time);
    }

    public List<PoolBooking> getBookingsByUserAndDate(Long userId, LocalDate date, String time) {
        return poolBookingRepository.findByUserIdAndDateAndTime(userId, date, time);
    }

    public void updateBooking(PoolBooking booking) {
        poolBookingRepository.save(booking);
    }
}
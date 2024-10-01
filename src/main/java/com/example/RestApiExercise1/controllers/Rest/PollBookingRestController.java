package com.example.RestApiExercise1.controllers.Rest;

import com.example.RestApiExercise1.models.PoolBooking;
import com.example.RestApiExercise1.models.User;
import com.example.RestApiExercise1.models.dto.BookingRequestDTO;
import com.example.RestApiExercise1.models.dto.PoolBookingDTO;
import com.example.RestApiExercise1.repositories.PoolBookingRepository;
import com.example.RestApiExercise1.repositories.UserRepository;
import com.example.RestApiExercise1.services.PoolBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/timetable")
@RestController
@RequiredArgsConstructor
public class PollBookingRestController {


    private final PoolBookingRepository poolBookingRepository;
    private final UserRepository userRepository;
    private final PoolBookingService poolBookingService;

    //Получение занятых записей на определенную дату
    @GetMapping("/all/{date}")
    public List<PoolBookingDTO> getAllBooking(@PathVariable LocalDate date) {
        List<PoolBooking> poolBookings = poolBookingRepository.findByDate(date);
        // Преобразуем каждый объект User в UserDTO
        return poolBookings.stream()
                .map(poolBooking -> new PoolBookingDTO(
                        poolBooking.getId()
                        , poolBooking.getDate()
                        , poolBooking.getTime()
                        , poolBooking.getCount())).collect(Collectors.toList());
    }

    // получение свободнах записей на определеную дату
    @GetMapping("/available/{date}")
    public List<PoolBookingDTO> getAllBookingByDate(@PathVariable LocalDate date) {
        List<PoolBooking> poolBookings = poolBookingRepository.findByDate(date);
        return poolBookings.stream()
                .map(poolBooking -> new PoolBookingDTO(
                        poolBooking.getDate()
                        , poolBooking.getTime()
                        , 10 - poolBooking.getCount())).collect(Collectors.toList());

    }

    // добовлении записи на время
    @PostMapping("/reserve")
    public ResponseEntity<String> addReserve(@RequestBody BookingRequestDTO bookingRequestDTO) {
        if (bookingRequestDTO.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID не должен быть пустым");
        }
        User user = userRepository.findById(bookingRequestDTO.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Такого пользователя по id не существует");
        }

        PoolBooking booking = new PoolBooking();
        booking.setUser(user);
        booking.setDate(bookingRequestDTO.getDate());
        booking.setTime(bookingRequestDTO.getTime());
        booking.setCount(bookingRequestDTO.getCount());
        booking.setOrderId(bookingRequestDTO.getOrderId());

        boolean success = poolBookingService.createBooking(booking);

        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Бронирование успешно создано");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("На выбранное время нет свободных мест. Пожалуйста, выберите другое время.");
        }
    }

    // отмена записи пользователя
    @PostMapping("/cancel/{userId}")
    public ResponseEntity<String> cancelReserve(@PathVariable Long userId, @RequestBody PoolBooking cancelBooking) {
        Optional<PoolBooking> bookingOptional = poolBookingRepository.findById(userId);
        if (bookingOptional.isPresent()) {
            PoolBooking existingBooking = bookingOptional.get();
            existingBooking.setOrderId(cancelBooking.getOrderId());
            existingBooking.setCount(cancelBooking.getCount());
            poolBookingRepository.save(existingBooking);
            return ResponseEntity.ok("Запись отменента ");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("у пользователя ни одной записи");
        }
    }
}



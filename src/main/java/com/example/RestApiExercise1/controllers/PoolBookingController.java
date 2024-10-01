package com.example.RestApiExercise1.controllers;

import com.example.RestApiExercise1.models.PoolBooking;
import com.example.RestApiExercise1.models.User;
import com.example.RestApiExercise1.services.PoolBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PoolBookingController {
    private final PoolBookingService poolBookingService;

    // Показать форму для записи на сеанс в бассейн
    @GetMapping("/booking")
    public String showBookingForm(Model model) {
        model.addAttribute("booking", new PoolBooking());
        return "booking";
    }


    @PostMapping("/booking")
    public String bookPoolSession(@ModelAttribute("booking") PoolBooking booking, Model model) {
        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Получаем все бронирования на указанную дату и время
        List<PoolBooking> bookingsAtThisTime = poolBookingService.getBookingsByDateAndTime(booking.getDate(), booking.getTime());

        // Подсчитываем общее количество бронирований на это время
        int totalBookings = bookingsAtThisTime.stream().mapToInt(PoolBooking::getCount).sum();

        // Проверяем, не превышено ли общее количество бронирований
        if (totalBookings >= 10) {
            model.addAttribute("errorMessage", "На выбранное время нет свободных мест. Пожалуйста, выберите другое время.");
            return "booking";
        }

        // Находим бронирование текущего пользователя, если оно существует
        PoolBooking userBooking = bookingsAtThisTime.stream()
                .filter(b -> b.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);

        int availableSpots = 10 - totalBookings;

        if (userBooking != null) {
            // Если запись существует, увеличиваем её количество, если есть место
            if (userBooking.getCount() < availableSpots) {
                userBooking.setCount(userBooking.getCount() + 1);
                poolBookingService.updateBooking(userBooking);
            } else {
                model.addAttribute("errorMessage", "Нет свободных мест для увеличения вашей записи.");
                return "booking";
            }
        } else {
            // Если записи нет, создаем новую
            if (availableSpots > 0) {
                booking.setUser(user);
                booking.setCount(1);
                poolBookingService.createBooking(booking);
            } else {
                model.addAttribute("errorMessage", "Нет свободных мест на выбранное время.");
                return "booking";
            }
        }

        return "redirect:/booking/success";
    }


    @GetMapping("/booking/success")
    public String showSuccessPage() {
        return "booking_success";
    }
}

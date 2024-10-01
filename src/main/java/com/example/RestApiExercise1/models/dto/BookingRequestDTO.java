package com.example.RestApiExercise1.models.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingRequestDTO {
    private Long userId;
    private Integer count;
    private LocalDate date;
    private String time;
    private String orderId;
}
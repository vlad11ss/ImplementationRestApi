package com.example.RestApiExercise1.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
@Table(name = "appointments")
public class PoolBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne // Связь с пользователем, который делает запись
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "time")
    private String time;
    @Column(name = "count")
    private Integer count = 0;
    @Column(name = "orderId")
    private String orderId;
}

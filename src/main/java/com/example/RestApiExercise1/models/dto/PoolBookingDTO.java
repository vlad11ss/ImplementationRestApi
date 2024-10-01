package com.example.RestApiExercise1.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL) // Игнорируем поля с null-значениями
public class PoolBookingDTO {

    private Long id;
    private Long user_id;
    private LocalDate date;
    private String time;
    private Integer count;

    public PoolBookingDTO() {
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public PoolBookingDTO(Long id, LocalDate date, String time, Integer count) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.count = count;
    }

    public PoolBookingDTO(LocalDate date, String time, Integer count) {

        this.date = date;
        this.time = time;
        this.count = count;
    }
}

package com.svalero.cinema.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScreeningInDto {
    private LocalDateTime screeningTime;
    private String theaterRoom;
    private double ticketPrice;
    private boolean subtitled;
    private Long movieId;
}

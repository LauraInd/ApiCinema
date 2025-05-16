package com.svalero.cinema.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningInDto {
    private LocalDateTime screeningTime;
    private String theaterRoom;
    private double ticketPrice;
    private boolean subtitled;
    private Long movieId;
}

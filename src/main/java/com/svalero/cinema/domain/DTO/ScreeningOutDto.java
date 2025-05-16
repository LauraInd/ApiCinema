package com.svalero.cinema.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScreeningOutDto {
    private Long id;
    private LocalDateTime screeningTime;
    private String theaterRoom;
    private double ticketPrice;
    private boolean subtitled;
    private String movieTitle;
}

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(LocalDateTime screeningTime) {
        this.screeningTime = screeningTime;
    }

    public String getTheaterRoom() {
        return theaterRoom;
    }

    public void setTheaterRoom(String theaterRoom) {
        this.theaterRoom = theaterRoom;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public boolean isSubtitled() {
        return subtitled;
    }

    public void setSubtitled(boolean subtitled) {
        this.subtitled = subtitled;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
}

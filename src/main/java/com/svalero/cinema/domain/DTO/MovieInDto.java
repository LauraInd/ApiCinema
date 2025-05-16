package com.svalero.cinema.domain.DTO;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInDto {

    private Long id;
    private String title;
    private String genre;
    private int durationMinutes;
    private LocalDate releaseDate;
    private boolean currentlyShowing;
}

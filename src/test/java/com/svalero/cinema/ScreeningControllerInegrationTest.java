package com.svalero.cinema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.domain.dto.ScreeningInDto;
import com.svalero.cinema.repository.MovieRepository;
import com.svalero.cinema.repository.ScreeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ScreeningControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long movieId;

    @BeforeEach
    void setUp() {
        screeningRepository.deleteAll();
        movieRepository.deleteAll();

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setGenre("Action");
        movie.setReleaseDate(LocalDate.of(2022, 1, 1));
        movie.setDurationMinutes(120);
        movie.setCurrentlyShowing(true);

        Movie savedMovie = movieRepository.save(movie);
        movieId = savedMovie.getId();
    }

    @Test
    void shouldCreateAndRetrieveScreening() throws Exception {
        // Crear screening
        ScreeningInDto screeningInDto = new ScreeningInDto();
        screeningInDto.setScreeningTime(LocalDateTime.now().plusDays(1));
        screeningInDto.setTheaterRoom("Sala 1");
        screeningInDto.setTicketPrice(9.99);
        screeningInDto.setSubtitled(true);
        screeningInDto.setMovieId(movieId);

        String jsonRequest = objectMapper.writeValueAsString(screeningInDto);

        String location = mockMvc.perform(post("/screenings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movieTitle").value("Test Movie"))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        // Obtener todos los screenings
        mockMvc.perform(get("/screenings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].theaterRoom").value("Sala 1"));
    }

    @Test
    void shouldReturn404WhenScreeningNotFound() throws Exception {
        mockMvc.perform(get("/screenings/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenPostInvalidScreening() throws Exception {
        ScreeningInDto invalid = new ScreeningInDto(); // sin datos obligatorios

        mockMvc.perform(post("/screenings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}

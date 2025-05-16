package com.svalero.cinema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.cinema.domain.DTO.MovieInDto;
import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    private Movie existingMovie;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
        existingMovie = new Movie(null, "Titanic", "Drama", 195, LocalDate.of(1997, 12, 19), true, null);
        existingMovie = movieRepository.save(existingMovie);
    }

    //  GET /movies/{id} → 200 OK
    @Test
    void shouldReturnMovieById() throws Exception {
        mockMvc.perform(get("/movies/" + existingMovie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Titanic"));
    }

    //  GET /movies/{id} → 404 Not Found
    @Test
    void shouldReturn404ForNonExistingMovie() throws Exception {
        mockMvc.perform(get("/movies/99999"))
                .andExpect(status().isNotFound());
    }

    //  POST /movies → 200 OK
    @Test
    void shouldCreateMovie() throws Exception {
        MovieInDto newMovie = new MovieInDto(null, "Inception", "Sci-Fi", 148, LocalDate.of(2010, 7, 16), true);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    //  POST /movies → 400 Bad Request
    @Test
    void shouldReturn400WhenCreatingInvalidMovie() throws Exception {
        MovieInDto invalidMovie = new MovieInDto(); // Empty DTO

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidMovie)))
                .andExpect(status().isBadRequest());
    }

    // PUT /movies/{id} → 200 OK
    @Test
    void shouldUpdateMovie() throws Exception {
        MovieInDto updated = new MovieInDto(null, "Titanic 2", "Action", 200, LocalDate.of(2025, 1, 1), false);

        mockMvc.perform(put("/movies/" + existingMovie.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Titanic 2"));
    }

    //  PUT /movies/{id} → 404 Not Found
    @Test
    void shouldReturn404WhenUpdatingNonExistentMovie() throws Exception {
        MovieInDto updated = new MovieInDto(null, "Ghost", "Fantasy", 120, LocalDate.of(2024, 1, 1), false);

        mockMvc.perform(put("/movies/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    //  DELETE /movies/{id} → 204 No Content
    @Test
    void shouldDeleteMovie() throws Exception {
        mockMvc.perform(delete("/movies/" + existingMovie.getId()))
                .andExpect(status().isNoContent());
    }

    //  DELETE /movies/{id} → 404 Not Found
    @Test
    void shouldReturn404WhenDeletingNonExistentMovie() throws Exception {
        mockMvc.perform(delete("/movies/99999"))
                .andExpect(status().isNotFound());
    }
}

package com.svalero.cinema.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.cinema.controller.MovieController;
import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.domain.dto.MovieInDto;
import com.svalero.cinema.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private MovieInDto movieInDto;
    private Movie movie;

    @BeforeEach
    void setUp() {
        movieInDto = new MovieInDto();
        movieInDto.setTitle("The Matrix");
        movieInDto.setGenre("Sci-Fi");
        movieInDto.setDurationMinutes(136);
        movieInDto.setReleaseDate(LocalDate.of(1999, 3, 31));
        movieInDto.setCurrentlyShowing(false);

        movie = new Movie(1L, "The Matrix", "Sci-Fi", 136, LocalDate.of(1999, 3, 31), false, null);
    }

    @Test
    void shouldGetAllMovies() throws Exception {
        when(movieService.findAll()).thenReturn(List.of(movie));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Matrix"));
    }

    @Test
    void shouldGetMovieById() throws Exception {
        when(movieService.findById(1L)).thenReturn(Optional.of(movie));

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Matrix"));
    }

    @Test
    void shouldReturn404WhenMovieNotFound() throws Exception {
        when(movieService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/movies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateMovie() throws Exception {
        when(movieService.create(any(MovieInDto.class))).thenReturn(movie);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Matrix"));
    }

    @Test
    void shouldUpdateMovie() throws Exception {
        when(movieService.update(eq(1L), any(MovieInDto.class))).thenReturn(movie);

        mockMvc.perform(put("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Matrix"));
    }

    @Test
    void shouldDeleteMovie() throws Exception {
        doNothing().when(movieService).delete(1L);

        mockMvc.perform(delete("/movies/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldReturn400WhenInvalidMovieData() throws Exception {
        MovieInDto invalidDto = new MovieInDto(); // campos obligatorios nulos

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

}

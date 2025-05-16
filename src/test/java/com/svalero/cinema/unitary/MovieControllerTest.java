package com.svalero.cinema.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.cinema.controller.MovieController;
import com.svalero.cinema.domain.dto.MovieInDto;
import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("The Matrix");
        movie.setGenre("Sci-Fi");
        movie.setDurationMinutes(136);
        movie.setReleaseDate(LocalDate.of(1999, 3, 31));
        movie.setCurrentlyShowing(true);
    }


    @Test
    void shouldReturnAllMovies() throws Exception {
        when(movieService.findAll()).thenReturn(List.of(movie));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Matrix"));
    }

    @Test
    void shouldReturnMovieById() throws Exception {
        when(movieService.findById(1L)).thenReturn(Optional.of(movie));

        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Matrix"));
    }

    // 📌 GET /movies/{id} → 404
    @Test
    void shouldReturn404WhenMovieNotFound() throws Exception {
        when(movieService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/movies/99"))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldFindMovieByTitle() throws Exception {
        when(movieService.findByTitle("The Matrix")).thenReturn(movie);

        mockMvc.perform(get("/movies/title/The Matrix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));
    }


    @Test
    void shouldFindMovieByGenre() throws Exception {
        when(movieService.findByGenre("Sci-Fi")).thenReturn(movie);

        mockMvc.perform(get("/movies/genre/Sci-Fi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Matrix"));
    }


    @Test
    void shouldFindByReleaseDate() throws Exception {
        LocalDate date = LocalDate.of(1999, 3, 31);
        when(movieService.findByReleaseDate(date)).thenReturn(List.of(movie));

        mockMvc.perform(get("/movies/release-date/1999-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Matrix"));
    }

    @Test
    void shouldCreateMovie() throws Exception {
        MovieInDto dto = new MovieInDto(null, "New Movie", "Action", 120, LocalDate.now(), true);
        Movie created = new Movie(2L, "New Movie", "Action", 120, LocalDate.now(), true, null);

        when(movieService.create(any(MovieInDto.class))).thenReturn(created);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Movie"));
    }

    @Test
    void shouldUpdateMovie() throws Exception {
        MovieInDto updateDto = new MovieInDto(null, "Updated", "Drama", 100, LocalDate.now(), false);
        Movie updated = new Movie(1L, "Updated", "Drama", 100, LocalDate.now(), false, null);

        when(movieService.update(Mockito.eq(1L), any(MovieInDto.class))).thenReturn(updated);

        mockMvc.perform(put("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void shouldDeleteMovie() throws Exception {
        mockMvc.perform(delete("/movies/1"))
                .andExpect(status().isNoContent());
    }
}

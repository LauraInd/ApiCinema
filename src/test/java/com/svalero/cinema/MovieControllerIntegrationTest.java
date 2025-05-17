package com.svalero.cinema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.cinema.domain.dto.MovieInDto;
import com.svalero.cinema.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.svalero.cinema.controller.MovieController;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieService movieService;


    @Test
    void shouldReturn200WhenGetMovies() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenInvalidPost() throws Exception {
        MovieInDto dto = new MovieInDto();
        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenMovieNotFound() throws Exception {
        mockMvc.perform(get("/movies/9999"))
                .andExpect(status().isNotFound());
    }
}

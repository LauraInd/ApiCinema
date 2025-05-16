package com.svalero.cinema.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.cinema.controller.ScreeningController;
import com.svalero.cinema.domain.dto.ScreeningInDto;
import com.svalero.cinema.domain.dto.ScreeningOutDto;
import com.svalero.cinema.exception.ScreeningNotFoundException;
import com.svalero.cinema.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScreeningController.class)
public class ScreeningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreeningService screeningService;

    @Autowired
    private ObjectMapper objectMapper;

    private ScreeningInDto screeningInDto;
    private ScreeningOutDto screeningOutDto;

    @BeforeEach
    void setUp() {
        screeningInDto = new ScreeningInDto();
        screeningInDto.setScreeningTime(LocalDateTime.of(2025, 5, 20, 18, 0));
        screeningInDto.setTheaterRoom("Room 1");
        screeningInDto.setTicketPrice(9.99);
        screeningInDto.setSubtitled(true);
        screeningInDto.setMovieId(1L);

        screeningOutDto = new ScreeningOutDto();
        screeningOutDto.setId(1L);
        screeningOutDto.setScreeningTime(screeningInDto.getScreeningTime());
        screeningOutDto.setTheaterRoom("Room 1");
        screeningOutDto.setTicketPrice(9.99);
        screeningOutDto.setSubtitled(true);
        screeningOutDto.setMovieTitle("Interstellar");
    }

    @Test
    void shouldGetAllScreenings() throws Exception {
        when(screeningService.findAll()).thenReturn(List.of(screeningOutDto));

        mockMvc.perform(get("/screenings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieTitle").value("Interstellar"));
    }

    @Test
    void shouldGetScreeningById() throws Exception {
        when(screeningService.findById(1L)).thenReturn(screeningOutDto);

        mockMvc.perform(get("/screenings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieTitle").value("Interstellar"));
    }

    @Test
    void shouldReturn404WhenScreeningNotFound() throws Exception {
        when(screeningService.findById(999L)).thenThrow(new ScreeningNotFoundException("Not found"));

        mockMvc.perform(get("/screenings/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateScreening() throws Exception {
        when(screeningService.add(any(ScreeningInDto.class))).thenReturn(screeningOutDto);

        mockMvc.perform(post("/screenings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(screeningInDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movieTitle").value("Interstellar"));
    }

    @Test
    void shouldUpdateScreening() throws Exception {
        when(screeningService.modify(eq(1L), any(ScreeningInDto.class))).thenReturn(screeningOutDto);

        mockMvc.perform(put("/screenings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(screeningInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieTitle").value("Interstellar"));
    }

    @Test
    void shouldDeleteScreening() throws Exception {
        doNothing().when(screeningService).delete(1L);

        mockMvc.perform(delete("/screenings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentScreening() throws Exception {
        doThrow(new ScreeningNotFoundException("Not found")).when(screeningService).delete(999L);

        mockMvc.perform(delete("/screenings/999"))
                .andExpect(status().isNotFound());
    }
}

package com.svalero.cinema.unitary;


import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.domain.Screening;
import com.svalero.cinema.domain.dto.ScreeningInDto;
import com.svalero.cinema.domain.dto.ScreeningOutDto;
import com.svalero.cinema.exception.ScreeningNotFoundException;
import com.svalero.cinema.repository.MovieRepository;
import com.svalero.cinema.repository.ScreeningRepository;
import com.svalero.cinema.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScreeningServiceTest {

    private ScreeningService screeningService;
    private ScreeningRepository screeningRepository;
    private MovieRepository movieRepository;
    private ModelMapper modelMapper;

    private Screening screening;
    private ScreeningInDto screeningInDto;
    private Movie movie;

    @BeforeEach
    void setUp() {
        screeningRepository = mock(ScreeningRepository.class);
        movieRepository = mock(MovieRepository.class);
        modelMapper = new ModelMapper();
        screeningService = new ScreeningService(screeningRepository, movieRepository, modelMapper);

        movie = new Movie(1L, "Inception", "Sci-Fi", 148, LocalDate.of(2010, 7, 16), true, null);

        screeningInDto = new ScreeningInDto();
        screeningInDto.setMovieId(1L);
        screeningInDto.setScreeningTime(LocalDateTime.of(2025, 6, 1, 20, 0));
        screeningInDto.setTheaterRoom("Room A");
        screeningInDto.setTicketPrice(10.5);
        screeningInDto.setSubtitled(true);

        screening = new Screening(null, screeningInDto.getScreeningTime(), screeningInDto.getTheaterRoom(),
                screeningInDto.getTicketPrice(), screeningInDto.isSubtitled(), movie);
    }

    @Test
    void shouldFindAllScreenings() {
        when(screeningRepository.findAll()).thenReturn(List.of(screening));
        List<ScreeningOutDto> result = screeningService.findAll();
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getMovieTitle());
    }

    @Test
    void shouldFindScreeningById() {
        screening.setId(1L);
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        ScreeningOutDto result = screeningService.findById(1L);
        assertEquals("Inception", result.getMovieTitle());
    }

    @Test
    void shouldThrowWhenScreeningNotFound() {
        when(screeningRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ScreeningNotFoundException.class, () -> screeningService.findById(99L));
    }

    @Test
    void shouldAddScreening() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(screeningRepository.save(any(Screening.class))).thenAnswer(inv -> {
            Screening s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        ScreeningOutDto result = screeningService.add(screeningInDto);
        assertEquals("Inception", result.getMovieTitle());
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldModifyScreening() {
        screening.setId(1L);
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(screeningRepository.save(any(Screening.class))).thenReturn(screening);

        ScreeningOutDto result = screeningService.modify(1L, screeningInDto);
        assertEquals("Inception", result.getMovieTitle());
    }

    @Test
    void shouldDeleteScreening() {
        screening.setId(1L);
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        doNothing().when(screeningRepository).deleteById(1L);

        assertDoesNotThrow(() -> screeningService.delete(1L));
        verify(screeningRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentScreening() {
        when(screeningRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ScreeningNotFoundException.class, () -> screeningService.delete(99L));
    }
}

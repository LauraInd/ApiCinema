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
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ScreeningService screeningService;

    private ScreeningInDto screeningInDto;
    private Screening screening;
    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie(1L, "Interstellar", "Sci-Fi", 169, LocalDate.of(2014, 11, 7), true, null);

        screeningInDto = new ScreeningInDto();
        screeningInDto.setMovieId(1L);
        screeningInDto.setScreeningTime(LocalDateTime.of(2025, 5, 20, 18, 0));
        screeningInDto.setTheaterRoom("Room 1");
        screeningInDto.setTicketPrice(9.99);
        screeningInDto.setSubtitled(true);

        screening = new Screening(1L, screeningInDto.getScreeningTime(), screeningInDto.getTheaterRoom(),
                screeningInDto.getTicketPrice(), screeningInDto.isSubtitled(), movie);
    }

    @Test
    void shouldReturnAllScreenings() {
        when(screeningRepository.findAll()).thenReturn(List.of(screening));
        when(modelMapper.map(screening, ScreeningOutDto.class)).thenReturn(new ScreeningOutDto());

        List<ScreeningOutDto> result = screeningService.findAll();

        assertEquals(1, result.size());
        verify(screeningRepository).findAll();
    }

    @Test
    void shouldReturnScreeningById() {
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        when(modelMapper.map(screening, ScreeningOutDto.class)).thenReturn(new ScreeningOutDto());

        ScreeningOutDto result = screeningService.findById(1L);

        assertNotNull(result);
        verify(screeningRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenScreeningNotFound() {
        when(screeningRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ScreeningNotFoundException.class, () -> screeningService.findById(999L));
    }

    @Test
    void shouldAddScreening() {
        when(modelMapper.map(screeningInDto, Screening.class)).thenReturn(screening);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(screeningRepository.save(screening)).thenReturn(screening);
        when(modelMapper.map(screening, ScreeningOutDto.class)).thenReturn(new ScreeningOutDto());

        ScreeningOutDto result = screeningService.add(screeningInDto);

        assertNotNull(result);
        verify(screeningRepository).save(screening);
    }

    @Test
    void shouldModifyScreening() {
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        doAnswer(invocation -> {
            ScreeningInDto source = invocation.getArgument(0);
            Screening destination = invocation.getArgument(1);
            destination.setScreeningTime(source.getScreeningTime());
            destination.setTheaterRoom(source.getTheaterRoom());
            destination.setTicketPrice(source.getTicketPrice());
            destination.setSubtitled(source.isSubtitled());
            return null;
        }).when(modelMapper).map(eq(screeningInDto), eq(screening));

        when(screeningRepository.save(screening)).thenReturn(screening);
        when(modelMapper.map(screening, ScreeningOutDto.class)).thenReturn(new ScreeningOutDto());

        ScreeningOutDto result = screeningService.modify(1L, screeningInDto);

        assertNotNull(result);
        verify(screeningRepository).save(screening);
    }


    @Test
    void shouldDeleteScreening() {
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        doNothing().when(screeningRepository).deleteById(1L);

        screeningService.delete(1L);

        verify(screeningRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentScreening() {
        when(screeningRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ScreeningNotFoundException.class, () -> screeningService.delete(99L));
        verify(screeningRepository, never()).deleteById(anyLong());
    }
}

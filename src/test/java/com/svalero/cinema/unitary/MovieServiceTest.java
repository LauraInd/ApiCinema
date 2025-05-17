
package com.svalero.cinema.unitary;

import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.domain.dto.MovieInDto;
import com.svalero.cinema.exception.MovieNotFoundException;
import com.svalero.cinema.repository.MovieRepository;
import com.svalero.cinema.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    private MovieService movieService;
    private MovieRepository movieRepository;
    private ModelMapper modelMapper;

    private Movie movie;
    private MovieInDto movieInDto;

    @BeforeEach
    void setUp() {
        movieRepository = mock(MovieRepository.class);
        modelMapper = new ModelMapper();
        movieService = new MovieService(movieRepository);
        movieService.setModelMapper(modelMapper);

        movie = new Movie(1L, "Inception", "Sci-Fi", 148, LocalDate.of(2010, 7, 16), true, new ArrayList<>());

        movieInDto = new MovieInDto();
        movieInDto.setTitle("Inception");
        movieInDto.setGenre("Sci-Fi");
        movieInDto.setDurationMinutes(148);
        movieInDto.setReleaseDate(LocalDate.of(2010, 7, 16));
        movieInDto.setCurrentlyShowing(true);
    }

    @Test
    void shouldFindAllMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        List<Movie> result = movieService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void shouldFindMovieById() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Optional<Movie> result = movieService.findById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void shouldFindMovieByTitle() {
        when(movieRepository.findByTitle("Inception")).thenReturn(movie);
        Movie result = movieService.findByTitle("Inception");
        assertEquals("Inception", result.getTitle());
    }

    @Test
    void shouldThrowIfMovieByTitleNotFound() {
        when(movieRepository.findByTitle("Nonexistent")).thenReturn(null);
        assertThrows(MovieNotFoundException.class, () -> movieService.findByTitle("Nonexistent"));
    }

    @Test
    void shouldFindMovieByGenre() {
        when(movieRepository.findByGenre("Sci-Fi")).thenReturn(movie);
        Movie result = movieService.findByGenre("Sci-Fi");
        assertEquals("Sci-Fi", result.getGenre());
    }

    @Test
    void shouldThrowIfMovieByGenreNotFound() {
        when(movieRepository.findByGenre("Comedy")).thenReturn(null);
        assertThrows(MovieNotFoundException.class, () -> movieService.findByGenre("Comedy"));
    }

    @Test
    void shouldCreateMovie() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        Movie result = movieService.create(movieInDto);
        assertEquals("Inception", result.getTitle());
    }

    @Test
    void shouldUpdateMovie() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        Movie result = movieService.update(1L, movieInDto);
        assertEquals("Inception", result.getTitle());
    }

    @Test
    void shouldThrowIfUpdatingNonexistentMovie() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(MovieNotFoundException.class, () -> movieService.update(99L, movieInDto));
    }

    @Test
    void shouldUpdatePartialMovie() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "New Title");

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        Movie result = movieService.updatePartial(1L, updates);

        assertEquals("New Title", result.getTitle());
    }

    @Test
    void shouldDeleteMovie() {
        when(movieRepository.existsById(1L)).thenReturn(true);
        doNothing().when(movieRepository).deleteById(1L);
        assertDoesNotThrow(() -> movieService.delete(1L));
    }

    @Test
    void shouldThrowIfDeletingNonexistentMovie() {
        when(movieRepository.existsById(99L)).thenReturn(false);
        assertThrows(MovieNotFoundException.class, () -> movieService.delete(99L));
    }
}

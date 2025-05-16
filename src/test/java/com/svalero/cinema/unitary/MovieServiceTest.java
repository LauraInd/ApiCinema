package com.svalero.cinema.unitary;

import com.svalero.cinema.domain.DTO.MovieInDto;
import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.exception.MovieNotFoundException;
import com.svalero.cinema.repository.MovieRepository;
import com.svalero.cinema.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movieService.setModelMapper(new ModelMapper());
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setDurationMinutes(148);
        movie.setReleaseDate(LocalDate.of(2010, 7, 16));
        movie.setCurrentlyShowing(true);
    }

    //  Test para obtener todas las películas
    @Test
    void testFindAllMovies() {
        List<Movie> movieList = List.of(movie);
        when(movieRepository.findAll()).thenReturn(movieList);

        List<Movie> result = movieService.findAll();

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    //  Test para obtener película por título
    @Test
    void testFindByTitleSuccess() {
        when(movieRepository.findByTitle("Inception")).thenReturn(movie);

        Movie result = movieService.findByTitle("Inception");

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository, times(1)).findByTitle("Inception");
    }

    //  Test para obtener película por título no encontrada
    @Test
    void testFindByTitleNotFound() {
        when(movieRepository.findByTitle("Unknown")).thenReturn(null);

        assertThrows(MovieNotFoundException.class, () -> movieService.findByTitle("Unknown"));
        verify(movieRepository, times(1)).findByTitle("Unknown");
    }

    //  Test para guardar una nueva película
    @Test
    void testCreateMovie() {
        MovieInDto movieInDto = new MovieInDto(null, "Interstellar", "Sci-Fi", 169, LocalDate.of(2014, 11, 7), true);
        Movie expected = new Movie(null, "Interstellar", "Sci-Fi", 169, LocalDate.of(2014, 11, 7), true, null);

        when(movieRepository.save(any(Movie.class))).thenReturn(expected);

        Movie result = movieService.create(movieInDto);

        assertEquals("Interstellar", result.getTitle());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }


    //  Test para actualizar película existente
    @Test
    void testUpdateMovieSuccess() {
        MovieInDto updateDto = new MovieInDto(null, "Updated Title", "Drama", 130, LocalDate.of(2020, 1, 1), false);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie result = movieService.update(1L, updateDto);

        assertEquals("Updated Title", result.getTitle());
        assertEquals("Drama", result.getGenre());
        verify(movieRepository, times(1)).save(movie);
    }


    //  Test para actualizar película no existente
    @Test
    void testUpdateMovieNotFound() {
        MovieInDto updateDto = new MovieInDto(null, "Nope", "None", 100, LocalDate.now(), true);
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.update(99L, updateDto));
        verify(movieRepository, never()).save(any(Movie.class));
    }

    // 📌 Test para eliminar una película existente
    @Test
    void testDeleteMovieSuccess() {
        when(movieRepository.existsById(1L)).thenReturn(true);
        doNothing().when(movieRepository).deleteById(1L);

        movieService.delete(1L);

        verify(movieRepository, times(1)).deleteById(1L);
    }

    // 📌 Test para eliminar una película que no existe
    @Test
    void testDeleteMovieNotFound() {
        when(movieRepository.existsById(99L)).thenReturn(false);

        assertThrows(MovieNotFoundException.class, () -> movieService.delete(99L));
        verify(movieRepository, never()).deleteById(anyLong());
    }

    //  Test para actualizar parcialmente una película
    @Test
    void testUpdatePartialMovie() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Partial Update");

        Movie updatedMovie = movieService.updatePartial(1L, updates);

        assertEquals("Partial Update", updatedMovie.getTitle());
        verify(movieRepository, times(1)).save(movie);
    }
}
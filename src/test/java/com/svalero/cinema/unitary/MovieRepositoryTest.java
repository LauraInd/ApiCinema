package com.svalero.cinema.unitary;

import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setTitle("The Matrix");
        movie.setGenre("Sci-Fi");
        movie.setDurationMinutes(136);
        movie.setReleaseDate(LocalDate.of(1999, 3, 31));
        movie.setCurrentlyShowing(false);

        movieRepository.save(movie);
    }

    @Test
    void shouldFindAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        assertFalse(movies.isEmpty());
        assertEquals("The Matrix", movies.get(0).getTitle());
    }

    @Test
    void shouldFindMovieByTitle() {
        Movie found = movieRepository.findByTitle("The Matrix");
        assertNotNull(found);
        assertEquals("Sci-Fi", found.getGenre());
    }

    @Test
    void shouldFindMovieByGenre() {
        Movie found = movieRepository.findByGenre("Sci-Fi");
        assertNotNull(found);
        assertEquals("The Matrix", found.getTitle());
    }

    @Test
    void shouldFindByReleaseDate() {
        List<Movie> movies = movieRepository.findByReleaseDate(LocalDate.of(1999, 3, 31));
        assertFalse(movies.isEmpty());
        assertEquals("The Matrix", movies.get(0).getTitle());
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        Movie notFound = movieRepository.findByTitle("Nonexistent");
        assertNull(notFound);

        Movie notFoundGenre = movieRepository.findByGenre("Fantasy");
        assertNull(notFoundGenre);

        List<Movie> emptyList = movieRepository.findByReleaseDate(LocalDate.of(2025, 1, 1));
        assertTrue(emptyList.isEmpty());
    }
}

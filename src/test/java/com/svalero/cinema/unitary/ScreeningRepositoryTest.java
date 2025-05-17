package com.svalero.cinema.unitary;

import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.domain.Screening;
import com.svalero.cinema.repository.MovieRepository;
import com.svalero.cinema.repository.ScreeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ScreeningRepositoryTest {

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        // Crear y guardar película
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setGenre("Drama");
        movie.setDurationMinutes(120);
        movie.setReleaseDate(LocalDateTime.now().minusYears(1).toLocalDate());
        movie.setCurrentlyShowing(true);
        movie = movieRepository.save(movie);  // ✅ ¡GUARDAR primero!

        // Crear screenings asociados a película ya persistida
        Screening screening1 = new Screening(null, LocalDateTime.now().plusDays(1), "Room 1", 8.5, true, movie);
        Screening screening2 = new Screening(null, LocalDateTime.now().plusDays(2), "Room 2", 9.0, false, movie);
        Screening screening3 = new Screening(null, LocalDateTime.now().plusDays(3), "Room 1", 10.0, true, movie);

        screeningRepository.saveAll(List.of(screening1, screening2, screening3));
    }

    @Test
    void shouldFindByTheaterRoom() {
        Movie movie = new Movie(null, "Interstellar", "Sci-Fi", 169, LocalDate.of(2014, 11, 7), true, null);
        movie = movieRepository.save(movie);
        Screening screening = new Screening();
        screening.setTheaterRoom("Room 1");
        screening.setTicketPrice(10.0);
        screening.setScreeningTime(LocalDateTime.now().plusDays(1));
        screening.setSubtitled(true);
        screening.setMovie(movie);

        screeningRepository.save(screening);

        List<Screening> result = screeningRepository.findByTheaterRoom("Room 1");

        assertFalse(result.isEmpty());
    }

    @Test
    void shouldFindSubtitledScreenings() {
        List<Screening> results = screeningRepository.findBySubtitledTrue();
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(Screening::isSubtitled));
    }

    @Test
    void shouldFindFutureScreenings() {
        LocalDateTime threshold = LocalDateTime.now().plusHours(12);
        List<Screening> results = screeningRepository.findByScreeningTimeAfter(threshold);

        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(s -> s.getScreeningTime().isAfter(threshold)));
    }
}

package com.svalero.cinema.service;

import com.svalero.cinema.domain.DTO.MovieInDto;
import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.exception.MovieNotFoundException;
import com.svalero.cinema.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Obtener todas las películas
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        return movies;
    }

    // Buscar por ID
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    // Buscar por título
    public Movie findByTitle(String title) {
        Movie movie = movieRepository.findByTitle(title);
        if (movie == null) {
            throw new MovieNotFoundException("Movie not found with title: " + title);
        }
        return movie;
    }

    // Buscar por género
    public Movie findByGenre(String genre) {
        Movie movie = movieRepository.findByGenre(genre);
        if (movie == null) {
            throw new MovieNotFoundException("Movie not found with genre: " + genre);
        }
        return movie;
    }

    public List<Movie> findByReleaseDate(LocalDate releaseDate) {
        return movieRepository.findByReleaseDate(releaseDate);
    }


    // Crear nueva película
    public Movie create(MovieInDto movieInDto) {
        Movie movie = modelMapper.map(movieInDto, Movie.class);
        return movieRepository.save(movie);
    }

    // Actualizar película completa
    public Movie update(Long id, MovieInDto movieInDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));

        modelMapper.map(movieInDto, movie); // Sobrescribe los campos del objeto existente
        return movieRepository.save(movie);
    }

    // Actualización parcial (PATCH)
    public Movie updatePartial(Long id, Map<String, Object> updates) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Movie.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, movie, value);
            }
        });

        return movieRepository.save(movie);
    }

    // Eliminar película
    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }
}

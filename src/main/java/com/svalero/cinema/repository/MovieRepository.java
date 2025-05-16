package com.svalero.cinema.repository;

import com.svalero.cinema.domain.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    // Método para obtener todos los usuarios
    List<Movie> findAll();

    // Método para buscar una pelicula por su titulo
    Movie findByTitle(String title);

    Movie findByGenre(String genre);
}


package com.svalero.cinema.repository;

import com.svalero.cinema.domain.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {
}

package com.svalero.cinema.repository;

import com.svalero.cinema.domain.Screening;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends CrudRepository<Screening, Long> {

    List<Screening> findByTheaterRoom(String theaterRoom);

    List<Screening> findBySubtitledTrue();

    List<Screening> findByScreeningTimeAfter(LocalDateTime dateTime);

}

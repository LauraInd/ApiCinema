package com.svalero.cinema.service;

import com.svalero.cinema.domain.Movie;
import com.svalero.cinema.domain.Screening;
import com.svalero.cinema.domain.DTO.ScreeningInDto;
import com.svalero.cinema.domain.DTO.ScreeningOutDto;
import com.svalero.cinema.exception.ScreeningNotFoundException;
import com.svalero.cinema.repository.MovieRepository;
import com.svalero.cinema.repository.ScreeningRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;

    public ScreeningService(ScreeningRepository screeningRepository, MovieRepository movieRepository, ModelMapper modelMapper) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.modelMapper = modelMapper;
    }

    public List<ScreeningOutDto> findAll() {
        List<Screening> screenings = (List<Screening>) screeningRepository.findAll();
        return screenings.stream().map(this::convertToOutDto).collect(Collectors.toList());
    }

    public ScreeningOutDto findById(Long id) {
        Screening screening = screeningRepository.findById(id).orElseThrow(ScreeningNotFoundException::new);
        return convertToOutDto(screening);
    }

    public ScreeningOutDto add(ScreeningInDto inDto) {
        Screening screening = convertToEntity(inDto);
        screening = screeningRepository.save(screening);
        return convertToOutDto(screening);


    }

    public ScreeningOutDto modify(Long id, ScreeningInDto inDto) {
        Screening existing = screeningRepository.findById(id).orElseThrow(ScreeningNotFoundException::new);
        modelMapper.map(inDto, existing);
        Movie movie = movieRepository.findById(inDto.getMovieId()).orElseThrow();
        existing.setMovie(movie);
        screeningRepository.save(existing);
        return convertToOutDto(existing);
    }

    public void delete(Long id) {
        screeningRepository.findById(id).orElseThrow(ScreeningNotFoundException::new);
        screeningRepository.deleteById(id);
    }


    private Screening convertToEntity(ScreeningInDto dto) {
        Screening screening = modelMapper.map(dto, Screening.class);
        Movie movie = movieRepository.findById(dto.getMovieId()).orElseThrow();
        screening.setMovie(movie);
        return screening;
    }

    private ScreeningOutDto convertToOutDto(Screening screening) {
        ScreeningOutDto dto = modelMapper.map(screening, ScreeningOutDto.class);
        dto.setMovieTitle(screening.getMovie().getTitle());
        return dto;
    }
}

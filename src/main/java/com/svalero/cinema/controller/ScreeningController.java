package com.svalero.cinema.controller;

import com.svalero.cinema.domain.DTO.ScreeningInDto;
import com.svalero.cinema.domain.DTO.ScreeningOutDto;
import com.svalero.cinema.exception.ScreeningNotFoundException;
import com.svalero.cinema.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScreeningController {

    @Autowired
    private ScreeningService screeningService;

    @GetMapping("/screenings")
    public ResponseEntity<List<ScreeningOutDto>> getAllScreenings() {
        List<ScreeningOutDto> screenings = screeningService.findAll();
        return ResponseEntity.ok(screenings);
    }

    @GetMapping("/screenings/{screeningId}")
    public ResponseEntity<ScreeningOutDto> getScreeningById(@PathVariable Long screeningId) throws ScreeningNotFoundException {
        ScreeningOutDto screening = screeningService.findById(screeningId);
        return ResponseEntity.ok(screening);
    }

    @PostMapping("/screenings")
    public ResponseEntity<ScreeningOutDto>addScreening(@RequestBody ScreeningInDto screeningInDto) throws ScreeningNotFoundException{
        ScreeningOutDto addScreening = screeningService.add(screeningInDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addScreening);
    }
    @PutMapping("/screenings/{screeningId}")
    public ResponseEntity<ScreeningOutDto> modifyScreening(@PathVariable Long screeningId, @RequestBody ScreeningInDto screeningInDto) throws ScreeningNotFoundException{
        ScreeningOutDto modifyScreening = screeningService.modify(screeningId, screeningInDto);
        return new ResponseEntity<>(modifyScreening, HttpStatus.OK);
    }
    @DeleteMapping("/screenings/{screeningId}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long screeningId) throws ScreeningNotFoundException{
        screeningService.delete(screeningId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(ScreeningNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}

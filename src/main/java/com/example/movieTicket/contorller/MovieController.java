package com.example.movieTicket.contorller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieTicket.Dtos.MovieRequestDto;
import com.example.movieTicket.Dtos.MovieResponseDto;
import com.example.movieTicket.service.MovieService;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/addMovie")
    public ResponseEntity<MovieResponseDto> addMovie(@RequestBody MovieRequestDto request) {
        return new ResponseEntity<>(movieService.addMovie(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable int id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
}
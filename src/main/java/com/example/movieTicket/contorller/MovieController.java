package com.example.movieTicket.contorller;

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
    public MovieResponseDto addMovie(@RequestBody MovieRequestDto request) {
        return movieService.addMovie(request);
    }
}
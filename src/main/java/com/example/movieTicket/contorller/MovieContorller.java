package com.example.movieTicket.contorller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieTicket.Dtos.MovieRequestDto;
import com.example.movieTicket.service.MovieService;

@RestController
@RequestMapping("movie")
public class MovieContorller {

    private MovieService movieService;

    public MovieContorller(MovieService movieService){
        this.movieService = movieService;
    }

    @RequestMapping("addMovie")
    public String addMovie(@RequestBody MovieRequestDto request){
        return movieService.addMovie(request);
    }
    
}

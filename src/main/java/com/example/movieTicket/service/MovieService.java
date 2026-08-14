package com.example.movieTicket.service;

import org.springframework.stereotype.Service;

import com.example.movieTicket.Dtos.MovieRequestDto;
import com.example.movieTicket.entity.Movie;

import com.example.movieTicket.repository.MovieRepository;


@Service
public class MovieService {
    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public String addMovie(MovieRequestDto movieRequest){
        if(movieRepository.existsByMovieName(movieRequest.getMovieName())){
            throw new RuntimeException("this movie already exist");
        }

        Movie newMovie = new Movie();
        newMovie.setMovieName(movieRequest.getMovieName());
        newMovie.setDuration(movieRequest.getDuration());
        newMovie.setGenre(movieRequest.getGenre());
        newMovie.setLanguage(movieRequest.getLanguage());
        newMovie.setReleaseDate(movieRequest.getReleaseDate());
        newMovie.setRating(movieRequest.getRating());

        movieRepository.save(newMovie);

        return newMovie.getMovieName();
                            
    }

    
}

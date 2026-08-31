package com.example.movieTicket.service;

import org.springframework.stereotype.Service;

import com.example.movieTicket.Dtos.MovieRequestDto;
import com.example.movieTicket.Dtos.MovieResponseDto;
import com.example.movieTicket.entity.Movie;
import com.example.movieTicket.repository.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieResponseDto addMovie(MovieRequestDto movieRequest) {
        if (movieRepository.existsByMovieName(movieRequest.getMovieName())) {
            throw new RuntimeException("this movie already exist");
        }

        Movie newMovie = new Movie();
        newMovie.setMovieName(movieRequest.getMovieName());
        newMovie.setDuration(movieRequest.getDuration());
        newMovie.setGenre(movieRequest.getGenre());
        newMovie.setLanguage(movieRequest.getLanguage());
        newMovie.setReleaseDate(movieRequest.getReleaseDate());
        newMovie.setRating(movieRequest.getRating());

        // Save to DB to generate auto-increment ID
        Movie savedMovie = movieRepository.save(newMovie);

        // Map saved entity to MovieResponseDto
        MovieResponseDto response = new MovieResponseDto();
        response.setId(savedMovie.getId());
        response.setMovieName(savedMovie.getMovieName());
        response.setDuration(savedMovie.getDuration());
        response.setRating(savedMovie.getRating());
        response.setReleaseDate(savedMovie.getReleaseDate());
        response.setGenre(savedMovie.getGenre());
        response.setLanguage(savedMovie.getLanguage());

        return response;
    }
}
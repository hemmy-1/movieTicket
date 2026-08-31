package com.example.movieTicket.service;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new RuntimeException("This movie already exists");
        }

        Movie newMovie = new Movie();
        newMovie.setMovieName(movieRequest.getMovieName());
        newMovie.setDuration(movieRequest.getDuration());
        newMovie.setGenre(movieRequest.getGenre());
        newMovie.setLanguage(movieRequest.getLanguage());
        newMovie.setReleaseDate(movieRequest.getReleaseDate());
        newMovie.setRating(movieRequest.getRating());

        Movie savedMovie = movieRepository.save(newMovie);
        return mapToResponseDto(savedMovie);
    }

    public MovieResponseDto getMovieById(int id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));
        return mapToResponseDto(movie);
    }

    public List<MovieResponseDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Helper method to convert Movie entity to MovieResponseDto
    private MovieResponseDto mapToResponseDto(Movie movie) {
        MovieResponseDto response = new MovieResponseDto();
        response.setId(movie.getId());
        response.setMovieName(movie.getMovieName());
        response.setDuration(movie.getDuration());
        response.setRating(movie.getRating());
        response.setReleaseDate(movie.getReleaseDate());
        response.setGenre(movie.getGenre());
        response.setLanguage(movie.getLanguage());
        return response;
    }
}
package com.example.movieTicket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movieTicket.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer > {
    Optional<Movie> findMovieByName(String movieName);
    boolean existsByMovieName(String movieName);
    
    
}

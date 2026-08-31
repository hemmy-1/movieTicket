package com.example.movieTicket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movieTicket.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Integer> {

    
    
}

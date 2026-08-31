package com.example.movieTicket.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movieTicket.entity.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Integer> {

    // Fetch all screens belongs to a specific theater
    List<Screen> findByTheaterId(Integer theaterId);

    // Fetch screen with its default master seats fetched eagerly
    @Query("SELECT s FROM Screen s LEFT JOIN FETCH s.screenSeats WHERE s.id = :screenId")
    Optional<Screen> findByIdWithSeats(@Param("screenId") Integer screenId);
}
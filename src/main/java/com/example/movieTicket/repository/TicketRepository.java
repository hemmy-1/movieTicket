package com.example.movieTicket.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movieTicket.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByUserId(UUID userId);

    Optional<Ticket> findByIdAndUserId(int id, UUID userId);
    
}

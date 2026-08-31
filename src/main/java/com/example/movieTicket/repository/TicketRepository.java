package com.example.movieTicket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movieTicket.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    
}

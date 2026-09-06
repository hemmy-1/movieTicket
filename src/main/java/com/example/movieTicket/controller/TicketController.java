package com.example.movieTicket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieTicket.Dtos.BookTicketRequestDto;
import com.example.movieTicket.entity.Ticket;
import com.example.movieTicket.service.TicketService;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/book")
    public ResponseEntity<Ticket> bookTicket(@RequestBody BookTicketRequestDto request, Authentication authentication) {
        return ResponseEntity.ok(ticketService.bookTicket(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getMyTickets(Authentication authentication) {
        return ResponseEntity.ok(ticketService.getUserTickets(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable int id, Authentication authentication) {
        return ResponseEntity.ok(ticketService.getTicketById(id, authentication.getName()));
    }
}
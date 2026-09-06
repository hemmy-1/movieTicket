package com.example.movieTicket.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieTicket.Dtos.BookTicketRequestDto;
import com.example.movieTicket.entity.Show;
import com.example.movieTicket.entity.Ticket;
import com.example.movieTicket.entity.Users;
import com.example.movieTicket.repository.ShowRepository;
import com.example.movieTicket.repository.TicketRepository;
import com.example.movieTicket.repository.UserRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository,
            ShowRepository showRepository,
            UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.showRepository = showRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Ticket bookTicket(BookTicketRequestDto request, String username) {
        Users user = userRepository.findByUserNameOrMobileNo(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found"));

        Ticket ticket = new Ticket();
        ticket.setShow(show);
        ticket.setUser(user);
        ticket.setSeatNumber(request.getSeatNumber());
        ticket.setPrice(request.getPrice());
        ticket.setBookingTime(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getUserTickets(String username) {
        Users user = userRepository.findByUserNameOrMobileNo(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ticketRepository.findByUserId(user.getId());
    }

    public Ticket getTicketById(int ticketId, String username) {
        Users user = userRepository.findByUserNameOrMobileNo(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ticketRepository.findByIdAndUserId(ticketId, user.getId())
                .orElseThrow(() -> new RuntimeException("Ticket not found or unauthorized"));
    }
}
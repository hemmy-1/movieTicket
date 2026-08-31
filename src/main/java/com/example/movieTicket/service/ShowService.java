package com.example.movieTicket.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movieTicket.Dtos.BookingRequestDto;
import com.example.movieTicket.Dtos.ShowRequestDto;
import com.example.movieTicket.entity.Movie;
import com.example.movieTicket.entity.Show;
import com.example.movieTicket.entity.ShowSeat;
import com.example.movieTicket.entity.Ticket;
import com.example.movieTicket.entity.Users;
import com.example.movieTicket.repository.MovieRepository;
import com.example.movieTicket.repository.ShowRepository;
import com.example.movieTicket.repository.ShowSeatRepository;
import com.example.movieTicket.repository.TicketRepository;
import com.example.movieTicket.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    private UserRepository userRepository;
    private TicketRepository ticketRepository;



    public ShowService(ShowRepository showRepository, MovieRepository movieRepository,
            ShowSeatRepository showSeatRepository, UserRepository userRepository, TicketRepository ticketRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.showSeatRepository = showSeatRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Show addShow(ShowRequestDto request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + request.getMovieId()));

        Show show = new Show();
        show.setTime(request.getTime());
        show.setMovie(movie);

        return showRepository.save(show);
    }

    @Transactional
    public Ticket bookTicket(BookingRequestDto request) {
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found"));

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ShowSeat seat = showSeatRepository.findByIdWithLock(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.isBooked()) {
            throw new RuntimeException("Seat is already booked!");
        }

        seat.setBooked(true);
        showSeatRepository.save(seat);

        Ticket ticket = new Ticket();
        ticket.setShow(show);
        ticket.setUser(user); 
        ticket.setSeatNumber(seat.getSeatNumber());
        ticket.setPrice(seat.getPrice());
        ticket.setBookingTime(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

}

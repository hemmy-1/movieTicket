package com.example.movieTicket.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieTicket.Dtos.BookingRequestDto;
import com.example.movieTicket.Dtos.ShowRequestDto;
import com.example.movieTicket.Dtos.ShowSeatResponseDto;
import com.example.movieTicket.entity.Movie;
import com.example.movieTicket.entity.Screen;
import com.example.movieTicket.entity.ScreenSeat;
import com.example.movieTicket.entity.Show;
import com.example.movieTicket.entity.ShowSeat;
import com.example.movieTicket.entity.Ticket;
import com.example.movieTicket.entity.Users;
import com.example.movieTicket.repository.MovieRepository;
import com.example.movieTicket.repository.ScreenRepository;
import com.example.movieTicket.repository.ShowRepository;
import com.example.movieTicket.repository.ShowSeatRepository;
import com.example.movieTicket.repository.TicketRepository;
import com.example.movieTicket.repository.UserRepository;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final ShowSeatRepository showSeatRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public ShowService(ShowRepository showRepository,
            MovieRepository movieRepository,
            ScreenRepository screenRepository,
            ShowSeatRepository showSeatRepository,
            TicketRepository ticketRepository,
            UserRepository userRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.screenRepository = screenRepository;
        this.showSeatRepository = showSeatRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Show addShow(ShowRequestDto request) {
        Movie movie = movieRepository.findMovieBymovieName(request.getMovieName())
                .orElseThrow(() -> new RuntimeException("Movie not found with name: " + request.getMovieName()));

        Screen screen = screenRepository.findByIdWithSeats(request.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found with ID: " + request.getScreenId()));

        Show show = new Show();
        show.setTime(request.getShowTime());
        show.setMovie(movie);
        show.setScreen(screen);

        Show savedShow = showRepository.save(show);

        List<ShowSeat> showSeats = new ArrayList<>();
        for (ScreenSeat screenSeat : screen.getScreenSeats()) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setSeatNumber(screenSeat.getSeatNumber());
            showSeat.setPrice(screenSeat.getDefaultPrice());
            showSeat.setBooked(false);
            showSeat.setShow(savedShow);

            showSeats.add(showSeat);
        }

        List<ShowSeat> savedShowSeats = showSeatRepository.saveAll(showSeats);
        savedShow.setShowSeatslist(savedShowSeats);

        return savedShow;
    }

    public List<ShowSeatResponseDto> getSeatsForShow(Integer showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + showId));

        return show.getShowSeatslist().stream()
                .map(seat -> new ShowSeatResponseDto(
                        seat.getId(),
                        seat.getSeatNumber(),
                        seat.getPrice(),
                        seat.isBooked()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Ticket bookTicket(BookingRequestDto request) {
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found with ID: " + request.getShowId()));

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        ShowSeat seat = showSeatRepository.findByIdWithLock(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found with ID: " + request.getSeatId()));

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
package com.example.movieTicket.contorller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieTicket.Dtos.BookingRequestDto;
import com.example.movieTicket.Dtos.ShowRequestDto;
import com.example.movieTicket.Dtos.ShowSeatResponseDto;
import com.example.movieTicket.entity.Show;
import com.example.movieTicket.entity.Ticket;
import com.example.movieTicket.service.ShowService;

@RestController
@RequestMapping("/show")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @PostMapping("/addShow")
    public ResponseEntity<Show> addShow(@RequestBody ShowRequestDto request) {
        return new ResponseEntity<>(showService.addShow(request), HttpStatus.CREATED);
    }

    @GetMapping("/{showId}/seats")
    public ResponseEntity<List<ShowSeatResponseDto>> getSeatsForShow(@PathVariable Integer showId) {
        return ResponseEntity.ok(showService.getSeatsForShow(showId));
    }

    @PostMapping("/bookTicket")
    public ResponseEntity<Ticket> bookTicket(@RequestBody BookingRequestDto request) {
        return ResponseEntity.ok(showService.bookTicket(request));
    }
}
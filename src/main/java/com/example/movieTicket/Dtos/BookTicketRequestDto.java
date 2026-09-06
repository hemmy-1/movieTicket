package com.example.movieTicket.Dtos;

import lombok.Data;

@Data 
public class BookTicketRequestDto {
    private int showId;
    private String seatNumber;
    private Double price;
}

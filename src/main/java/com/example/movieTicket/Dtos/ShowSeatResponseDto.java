package com.example.movieTicket.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeatResponseDto {

    private int id;
    private String seatNumber;
    private Double price;
    private boolean isBooked;
}
package com.example.movieTicket.Dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {

    private Integer showId;
    private Integer seatId;
    private UUID userId;
}

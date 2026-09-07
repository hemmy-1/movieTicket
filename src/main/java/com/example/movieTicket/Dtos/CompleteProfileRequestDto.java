package com.example.movieTicket.Dtos;

import java.util.UUID;
import lombok.Data;

@Data
public class CompleteProfileRequestDto {
    private UUID userId;
    private String name;
   
}
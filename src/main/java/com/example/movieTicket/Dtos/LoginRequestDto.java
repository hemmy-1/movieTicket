package com.example.movieTicket.Dtos;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String identifier; 
    private String password;
}
package com.example.movieTicket.Dtos;

import com.example.movieTicket.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private Users user;
}
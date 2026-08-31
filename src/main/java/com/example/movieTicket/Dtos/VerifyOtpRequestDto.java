package com.example.movieTicket.Dtos;

import lombok.Data;

@Data
public class VerifyOtpRequestDto {
    private String mobileNo;
    private String otp;
}
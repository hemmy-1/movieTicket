package com.example.movieTicket.Dtos;

import java.time.LocalDateTime;

import com.example.movieTicket.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDto {
    private int paymentId;
    private String transactionRef;
    private Double amount;
    private String paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paymentTime;
    private int ticketId;
    private String movieTitle;
}
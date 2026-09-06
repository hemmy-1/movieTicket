package com.example.movieTicket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieTicket.Dtos.PaystackInitResponse;
import com.example.movieTicket.Dtos.PaymentResponseDto;
import com.example.movieTicket.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/paystack/init")
    public ResponseEntity<PaystackInitResponse> initializePaystack(@RequestParam int ticketId,
            Authentication authentication) {
        return ResponseEntity.ok(paymentService.initializePaystackPayment(ticketId, authentication.getName()));
    }

    @GetMapping("/verify/{reference}")
    public ResponseEntity<PaymentResponseDto> verifyPaystack(@PathVariable String reference) {
        return ResponseEntity.ok(paymentService.verifyPaystackPayment(reference));
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentHistory(Authentication authentication) {
        return ResponseEntity.ok(paymentService.getPaymentHistory(authentication.getName()));
    }
}
package com.example.movieTicket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieTicket.Dtos.AuthResponseDto;
import com.example.movieTicket.Dtos.CompleteProfileRequestDto;
import com.example.movieTicket.Dtos.RefreshTokenRequestDto;
import com.example.movieTicket.Dtos.SendOtpRequestDto;
import com.example.movieTicket.Dtos.VerifyOtpRequestDto;
import com.example.movieTicket.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody SendOtpRequestDto request) {
        return ResponseEntity.ok(authService.sendOtp(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponseDto> verifyOtp(@RequestBody VerifyOtpRequestDto request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<AuthResponseDto> completeProfile(@RequestBody CompleteProfileRequestDto request) {
        return ResponseEntity.ok(authService.completeProfile(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
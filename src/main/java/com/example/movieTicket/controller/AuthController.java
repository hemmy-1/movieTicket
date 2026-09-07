package com.example.movieTicket.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieTicket.Dtos.AuthResponseDto;
import com.example.movieTicket.Dtos.CompleteProfileRequestDto;
import com.example.movieTicket.Dtos.LoginRequestDto;
import com.example.movieTicket.Dtos.MeRequestDto;
import com.example.movieTicket.Dtos.MeResponseDto;
import com.example.movieTicket.Dtos.RefreshTokenRequestDto;
import com.example.movieTicket.Dtos.RegisterRequestDto;
import com.example.movieTicket.Dtos.RegisterResponseDto;
import com.example.movieTicket.Dtos.VerifyOtpRequestDto;
import com.example.movieTicket.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto request) {
        RegisterResponseDto response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

   

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody VerifyOtpRequestDto request) {
        return (authService.verifyOtp(request));
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<AuthResponseDto> completeProfile(@RequestBody CompleteProfileRequestDto request) {
        return ResponseEntity.ok(authService.completeProfile(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @GetMapping 
    public ResponseEntity<MeResponseDto> Me(@RequestBody MeRequestDto request){
        return ResponseEntity.ok(authService.Me(request));
    }
}
package com.example.movieTicket.service;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieTicket.Dtos.AuthResponseDto;
import com.example.movieTicket.Dtos.CompleteProfileRequestDto;
import com.example.movieTicket.Dtos.LoginRequestDto;
import com.example.movieTicket.Dtos.MeRequestDto;
import com.example.movieTicket.Dtos.MeResponseDto;
import com.example.movieTicket.Dtos.RefreshTokenRequestDto;
import com.example.movieTicket.Dtos.SendOtpRequestDto;
import com.example.movieTicket.Dtos.VerifyOtpRequestDto;
import com.example.movieTicket.Dtos.RegisterRequestDto;
import com.example.movieTicket.Dtos.RegisterResponseDto;
import com.example.movieTicket.entity.RefreshToken;
import com.example.movieTicket.entity.Users;
import com.example.movieTicket.repository.RefreshTokenRepository;
import com.example.movieTicket.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String sendOtp(SendOtpRequestDto request) {
        String generatedOtp = String.format("%06d", new Random().nextInt(900000) + 100000);

        Users user = userRepository.findByMobileNo(request.getMobileNo())
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setMobileNo(request.getMobileNo());
                    newUser.setVerified(false);
                    return newUser;
                });

        userRepository.save(user);

        // String messageBody = "Your verification code for MovieTicket is: " + generatedOtp;
        // smsService.sendSms(request.getMobileNo(), messageBody);

        return "Here is your OTP     " + generatedOtp;
    }

    @Transactional
    public String verifyOtp(VerifyOtpRequestDto request) {
        Users user = userRepository.findByMobileNo(request.getMobileNo())
                .orElseThrow(() -> new RuntimeException("Mobile number not found"));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP code!");
        }

        user.setVerified(true);
        user.setOtp(null);
        Users savedUser = userRepository.save(user);

      
        return "Otp sucessful";
    }

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByMobileNo(request.getMobileNo())) {
            throw new IllegalArgumentException("This number already exists");
        }

        // 1. Generate 6-digit OTP
        String generatedOtp = String.format("%06d", new Random().nextInt(900000) + 100000);

        // 2. Build and save new user
        Users newUser = new Users();
        newUser.setEmail(request.getEmail());
        newUser.setGender(request.getGender());
        newUser.setMobileNo(request.getMobileNo());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setAge(request.getAge());
        newUser.setOtp(generatedOtp); // Attach OTP to user
        newUser.setVerified(false);

        Users savedUser = userRepository.save(newUser);

        // 3. Optional: Trigger mock SMS service logger
        // smsService.sendSms(savedUser.getMobileNo(), generatedOtp);

        // 4. Return DTO with mobile number, OTP, and response message
        return new RegisterResponseDto(
                savedUser.getMobileNo(),
                generatedOtp,
                "User registered successfully. Please verify using the OTP.");
    }
    @Transactional
    public AuthResponseDto completeProfile(CompleteProfileRequestDto request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("User phone number is not verified!");
        }

        user.setUserName(request.getName());
       

      
        Users savedUser = userRepository.save(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);
        String accessToken = jwtService.generateToken(savedUser);

        return new AuthResponseDto(accessToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        return refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return new AuthResponseDto(accessToken, request.getRefreshToken());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @Transactional
    public AuthResponseDto login(LoginRequestDto request) {
        Users user = userRepository.findByUserNameOrMobileNo(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(() -> new RuntimeException("Invalid username/mobile number or password"));

        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username/mobile number or password");
        }

        if (!user.isVerified()) {
            throw new RuntimeException("Account is not verified");
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        String accessToken = jwtService.generateToken(user);

        return new AuthResponseDto(accessToken, refreshToken.getToken());
    }

    public MeResponseDto Me(MeRequestDto request){

        Users user = userRepository.findByUserName(request.getUserName()).orElseThrow(()-> new RuntimeException("User not found"));

        return new MeResponseDto(
            user.getId(),
            user.getGender(),
            user.getMobileNo(),
            user.getEmail(),
            user.getAge(),
            user.getTicketLists()
        );
    }
}
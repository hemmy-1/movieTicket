package com.example.movieTicket.service;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieTicket.Dtos.AuthResponseDto;
import com.example.movieTicket.Dtos.CompleteProfileRequestDto;
import com.example.movieTicket.Dtos.LoginRequestDto;
import com.example.movieTicket.Dtos.RefreshTokenRequestDto;
import com.example.movieTicket.Dtos.SendOtpRequestDto;
import com.example.movieTicket.Dtos.VerifyOtpRequestDto;
import com.example.movieTicket.entity.RefreshToken;
import com.example.movieTicket.entity.Users;
import com.example.movieTicket.repository.RefreshTokenRepository;
import com.example.movieTicket.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SmsService smsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
            SmsService smsService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.smsService = smsService;
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

        user.setOtp(generatedOtp);
        userRepository.save(user);

        // String messageBody = "Your verification code for MovieTicket is: " + generatedOtp;
        // smsService.sendSms(request.getMobileNo(), messageBody);

        return "Here is your OTP     " + generatedOtp;
    }

    @Transactional
    public AuthResponseDto verifyOtp(VerifyOtpRequestDto request) {
        Users user = userRepository.findByMobileNo(request.getMobileNo())
                .orElseThrow(() -> new RuntimeException("Mobile number not found"));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP code!");
        }

        user.setVerified(true);
        user.setOtp(null);
        Users savedUser = userRepository.save(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);
        String accessToken = jwtService.generateToken(savedUser);

        return new AuthResponseDto(accessToken, refreshToken.getToken(), savedUser);
    }

    @Transactional
    public AuthResponseDto completeProfile(CompleteProfileRequestDto request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("User phone number is not verified!");
        }

        user.setUserName(request.getName());
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());

        Users savedUser = userRepository.save(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);
        String accessToken = jwtService.generateToken(savedUser);

        return new AuthResponseDto(accessToken, refreshToken.getToken(), savedUser);
    }

    @Transactional
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        return refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return new AuthResponseDto(accessToken, request.getRefreshToken(), user);
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

        return new AuthResponseDto(accessToken, refreshToken.getToken(), user);
    }
}
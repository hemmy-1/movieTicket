package com.example.movieTicket.service;

import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieTicket.Dtos.CompleteProfileRequestDto;
import com.example.movieTicket.Dtos.SendOtpRequestDto;
import com.example.movieTicket.Dtos.VerifyOtpRequestDto;
import com.example.movieTicket.entity.Users;
import com.example.movieTicket.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        return "OTP sent successfully. (Mock OTP: " + generatedOtp + ")";
    }

    @Transactional
    public Users verifyOtp(VerifyOtpRequestDto request) {
        Users user = userRepository.findByMobileNo(request.getMobileNo())
                .orElseThrow(() -> new RuntimeException("Mobile number not found"));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP code!");
        }

        user.setVerified(true);
        user.setOtp(null);
        return userRepository.save(user);
    }

    @Transactional
    public Users completeProfile(CompleteProfileRequestDto request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("User phone number is not verified!");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setGender(request.getGender());

        return userRepository.save(user);
    }
}
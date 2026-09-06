package com.example.movieTicket.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movieTicket.entity.RefreshToken;
import com.example.movieTicket.entity.Users;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(Users user);
    
    int deleteByUser(Users user);
}
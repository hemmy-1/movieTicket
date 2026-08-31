package com.example.movieTicket.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movieTicket.entity.Users;

public interface UserRepository extends JpaRepository<Users, UUID> {
    
}

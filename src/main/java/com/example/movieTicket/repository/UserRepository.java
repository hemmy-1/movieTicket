package com.example.movieTicket.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movieTicket.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByMobileNo(String mobileNo);

    Optional<Users> findByEmail(String email);
}
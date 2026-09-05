package com.example.movieTicket.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movieTicket.entity.Users;

public interface UserRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByUserNameOrMobileNo(String userName, String mobileNo);
    Optional<Users> findByMobileNo(String mobileNo);
}
package com.example.movieTicket.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.movieTicket.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByUserIdOrderByPaymentTimeDesc(UUID userId);

    Optional<Payment> findByTransactionRef(String transactionRef);
}
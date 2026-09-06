package com.example.movieTicket.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.example.movieTicket.Dtos.PaystackInitRequest;
import com.example.movieTicket.Dtos.PaystackInitResponse;
import com.example.movieTicket.Dtos.PaymentResponseDto;
import com.example.movieTicket.entity.Payment;
import com.example.movieTicket.enums.PaymentStatus;
import com.example.movieTicket.entity.Ticket;
import com.example.movieTicket.entity.Users;
import com.example.movieTicket.repository.PaymentRepository;
import com.example.movieTicket.repository.TicketRepository;
import com.example.movieTicket.repository.UserRepository;

@Service
public class PaymentService {

    @Value("${paystack.secret.key}")
    private String paystackSecretKey;

    @Value("${paystack.init.url}")
    private String paystackInitUrl;

    @Value("${paystack.verify.url}")
    private String paystackVerifyUrl;

    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public PaymentService(PaymentRepository paymentRepository,
            TicketRepository ticketRepository,
            UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public PaystackInitResponse initializePaystackPayment(int ticketId, String username) {
        Users user = userRepository.findByUserNameOrMobileNo(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = ticketRepository.findByIdAndUserId(ticketId, user.getId())
                .orElseThrow(() -> new RuntimeException("Ticket not found or unauthorized"));

        if (ticket.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Ticket has already been paid for!");
        }

        String reference = "PAYSTACK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        long amountInSubunits = Math.round(ticket.getPrice() * 100);

        PaystackInitRequest paystackRequest = new PaystackInitRequest(
                user.getEmail() != null ? user.getEmail() : "user@example.com",
                String.valueOf(amountInSubunits),
                reference,
                "http://localhost:8080/api/v1/payments/verify/" + reference);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(paystackSecretKey);

        HttpEntity<PaystackInitRequest> entity = new HttpEntity<>(paystackRequest, headers);

        ResponseEntity<PaystackInitResponse> response = restTemplate.postForEntity(
                paystackInitUrl, entity, PaystackInitResponse.class);

        Payment payment = Payment.builder()
                .user(user)
                .ticket(ticket)
                .amount(ticket.getPrice())
                .paymentMethod("PAYSTACK_DEMO")
                .transactionRef(reference)
                .status(PaymentStatus.PENDING)
                .paymentTime(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return response.getBody();
    }

    @Transactional
    public PaymentResponseDto verifyPaystackPayment(String reference) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paystackSecretKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                paystackVerifyUrl + reference, HttpMethod.GET, entity, String.class);

        Payment payment = paymentRepository.findByTransactionRef(reference)
                .orElseThrow(() -> new RuntimeException("Transaction reference not found"));

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null
                && response.getBody().contains("\"status\":true")) {
            payment.setStatus(PaymentStatus.PAID);
            payment.getTicket().setPaymentStatus(PaymentStatus.PAID);
            ticketRepository.save(payment.getTicket());
        } else {
            payment.setStatus(PaymentStatus.CANCELLED);
        }

        paymentRepository.save(payment);
        return mapToDto(payment);
    }

    public List<PaymentResponseDto> getPaymentHistory(String username) {
        Users user = userRepository.findByUserNameOrMobileNo(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return paymentRepository.findByUserIdOrderByPaymentTimeDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PaymentResponseDto mapToDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getId())
                .transactionRef(payment.getTransactionRef())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paymentTime(payment.getPaymentTime())
                .ticketId(payment.getTicket().getId())
                .movieTitle(payment.getTicket().getShow() != null && payment.getTicket().getShow().getMovie() != null
                        ? payment.getTicket().getShow().getMovie().getMovieName()
                        : "N/A")
                .build();
    }
}
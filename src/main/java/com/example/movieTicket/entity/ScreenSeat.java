package com.example.movieTicket.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "screen_seats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String seatNumber; // e.g., "A1", "A2"
    private Double defaultPrice;

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
}
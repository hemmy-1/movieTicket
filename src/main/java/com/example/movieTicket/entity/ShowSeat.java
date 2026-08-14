package com.example.movieTicket.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ShowSeat {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int showId;

    private Date date;

    
    
}

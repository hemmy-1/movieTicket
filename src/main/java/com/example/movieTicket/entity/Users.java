package com.example.movieTicket.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.movieTicket.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users  {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private int age;

    private Gender gender;

    private String mobileNo;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> ticketLists  = new ArrayList<>();
    

    
}

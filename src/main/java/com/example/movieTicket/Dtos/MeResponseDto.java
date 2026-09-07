package com.example.movieTicket.Dtos;

import java.util.List;
import java.util.UUID;

import com.example.movieTicket.entity.Ticket;
import com.example.movieTicket.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class MeResponseDto {
    private UUID id;
    private Gender gender;
    private String mobileNo;
    private String email;
    private int age;
    private List<Ticket> ticketLists;
}

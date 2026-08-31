package com.example.movieTicket.Dtos;

import java.util.UUID;
import com.example.movieTicket.enums.Gender;
import lombok.Data;

@Data
public class CompleteProfileRequestDto {
    private UUID userId;
    private String name;
    private String email;
    private int age;
    private Gender gender;
}
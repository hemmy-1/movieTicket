package com.example.movieTicket.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.movieTicket.enums.Gender;


@Data 
@AllArgsConstructor // Generates the constructor accepting arguments
@NoArgsConstructor
public class RegisterRequestDto {
    private String email;
    private int age;
    private Gender gender;
    private String password;  
    private String mobileNo;
}

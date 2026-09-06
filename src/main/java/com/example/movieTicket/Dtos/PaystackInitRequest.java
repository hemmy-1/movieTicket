package com.example.movieTicket.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaystackInitRequest {
    private String email;
    private String amount; 
    
    private String reference;
    private String callback_url;
}
package com.example.movieTicket.Dtos;

import lombok.Data;

@Data
public class PaystackInitResponse {
    private boolean status;
    private String message;
    private Data data;

    @lombok.Data
    public static class Data {
        private String authorization_url;
        private String access_code;
        private String reference;
    }
}
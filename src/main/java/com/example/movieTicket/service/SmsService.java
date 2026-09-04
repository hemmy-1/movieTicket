package com.example.movieTicket.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

@Service
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String toMobileNo, String messageBody) {
    try {
        Message.creator(
                new PhoneNumber(toMobileNo),
                new PhoneNumber(fromPhoneNumber),
                messageBody).create();
    } catch (Exception e) {
        throw new RuntimeException("Failed to send SMS to " + toMobileNo + ": " + e.getMessage());
    }
}
}
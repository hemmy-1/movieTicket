package com.example.movieTicket.Dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowRequestDto {

    private String movieName;
    private Integer screenId;
    private LocalDateTime showTime;}
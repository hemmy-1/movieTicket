package com.example.movieTicket.Dtos;

import java.util.Date;

import com.example.movieTicket.enums.Genre;
import com.example.movieTicket.enums.Language;

import lombok.Data;

@Data
public class MovieResponseDto {
    private int id;
    private String movieName;
    private Integer duration;
    private Double rating;
    private Date releaseDate;
    private Genre genre;
    private Language language;

}

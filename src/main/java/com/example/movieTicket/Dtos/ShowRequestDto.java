

package com.example.movieTicket.Dtos;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowRequestDto {

    private Integer movieId;
    private LocalTime time;
}
package com.url.shortner.dtos;

import lombok.Data;

import java.time.LocalDate;
@Data
public class clickEventDto {
    private LocalDate clickDate;
    private long count;

}

package com.url.shortner.dtos;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UrlMappingDto {
    private Long id;
   private  String originalUrl;
   private String shortUrl;
   private int ClickCount;
   private LocalDateTime createdDate;
   private String username;

}

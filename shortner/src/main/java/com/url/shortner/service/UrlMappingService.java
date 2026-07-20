package com.url.shortner.service;

import com.url.shortner.Repository.ClickEventRepository;
import com.url.shortner.Repository.UrlMappingRepository;
import com.url.shortner.dtos.UrlMappingDto;
import com.url.shortner.dtos.clickEventDto;
import com.url.shortner.models.ClickEvent;
import com.url.shortner.models.URLMapping;
import com.url.shortner.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;

    public  List<clickEventDto> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        URLMapping urlMapping=urlMappingRepository.findByShortUrl(shortUrl);
        if(urlMapping!=null)
        {
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping,start,end).stream()
                    .collect(Collectors.groupingBy(click->click.getClickDate().toLocalDate(),Collectors.counting()))
                    .entrySet().stream().map(entry->{
                        clickEventDto clickEventDto =new clickEventDto();
                        clickEventDto.setClickDate(entry.getKey());
                        clickEventDto.setCount(entry.getValue());
                        return clickEventDto;
                    } ).collect(Collectors.toList());
        }
        return null;
    }

    public List<UrlMappingDto> getUrlsByUser(User user) {

        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .toList();
    }

    public  UrlMappingDto createShortUrl(String originalUrl, User user) {
        String shortUrl=generateShortUrl(originalUrl);
        URLMapping urlMapping=new URLMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        //urlMapping.setId(user.getId());
        URLMapping savedurlmapping=urlMappingRepository.save(urlMapping);
        return convertToDto(savedurlmapping);
    }
    private UrlMappingDto convertToDto(URLMapping urlMapping)
    {
        UrlMappingDto urlMappingDto=new UrlMappingDto();
        urlMappingDto.setId(urlMapping.getId());
        urlMappingDto.setShortUrl(urlMapping.getShortUrl());
        urlMappingDto.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDto.setClickCount(urlMapping.getClickCount());
        urlMappingDto.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDto.setUsername(urlMapping.getUser().getUsername());
        return urlMappingDto;

    }

    private static String generateShortUrl(String originalUrl) {
        Random random=new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder shortUrl=new StringBuilder(8);
        for(int i=0;i<8;i++)
        {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortUrl.toString();
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<URLMapping>urlMappings=urlMappingRepository.findByUser(user);
        List<ClickEvent>clickEvents=clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings,start.atStartOfDay(),end.plusDays(1).atStartOfDay());
        return clickEvents.stream()
                .collect(Collectors.groupingBy(click->click.getClickDate().toLocalDate(),Collectors.counting()));
    }
 
    public URLMapping getOriginalUrl(String shortUrl) {
        URLMapping urlMapping=urlMappingRepository.findByShortUrl(shortUrl);
        if(urlMapping!=null)
        {
            urlMapping.setClickCount(urlMapping.getClickCount()+1);
            urlMappingRepository.save(urlMapping);
            ClickEvent clickEvent=new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvent);
        }
        return urlMapping;
    }
}

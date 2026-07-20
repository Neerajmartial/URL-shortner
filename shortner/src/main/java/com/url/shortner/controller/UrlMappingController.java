package com.url.shortner.controller;

import com.url.shortner.dtos.UrlMappingDto;
import com.url.shortner.dtos.clickEventDto;
import com.url.shortner.models.User;
import com.url.shortner.service.UrlMappingService;
import com.url.shortner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
public class UrlMappingController {
    private final UrlMappingService urlMappingService;
    private final UserService userService;
    public UrlMappingController(UrlMappingService urlMappingService,UserService userService1)
    {
        this.urlMappingService=urlMappingService;
        this.userService=userService1;
    }
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDto>createShortUrl(@RequestBody Map<String,String> request, Principal principal)
    {
        String originalUrl=request.get("originalUrl");
        User user=userService.findByUsername(principal.getName());
        UrlMappingDto urlMappingDto=urlMappingService.createShortUrl(originalUrl,user);
        return ResponseEntity.ok(urlMappingDto);
    }
    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDto>>getUserUrls(Principal principal)
    {
        User user=userService.findByUsername(principal.getName());
        List<UrlMappingDto>urls=urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }
    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<clickEventDto>>getUrlAnalytics(@PathVariable String shortUrl,
                                                        @RequestParam("startDate") String startDate,
                                                        @RequestParam("endDate") String endDate)
    {
        DateTimeFormatter formatter=DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start=LocalDateTime.parse(startDate,formatter);
        LocalDateTime end=LocalDateTime.parse(endDate,formatter);
        List<clickEventDto>clickEventDto=urlMappingService.getClickEventsByDate(shortUrl,start,end);
        return ResponseEntity.ok(clickEventDto);
    }
    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate,Long>>getTotalClicksByDate(Principal principal,@RequestParam("startDate") String startDate,
                                                                   @RequestParam("endDate") String endDate)
    {
        DateTimeFormatter formatter=DateTimeFormatter.ISO_LOCAL_DATE;
        User user=userService.findByUsername(principal.getName());
        LocalDate start=LocalDate.parse(startDate,formatter);
        LocalDate end=LocalDate.parse(endDate,formatter);
        Map<LocalDate,Long>totalClicks=urlMappingService.getTotalClicksByUserAndDate(user,start,end);
        return ResponseEntity.ok(totalClicks);
    }

}

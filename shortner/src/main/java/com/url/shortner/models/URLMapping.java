package com.url.shortner.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import org.jspecify.annotations.Nullable;

@Entity
@Data
@Table(name="UrlMappings")
public class URLMapping {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @Column(name = "original_url", length = 2048)
    private String originalUrl;

    @Column(name = "click_count")
    private int clickCount = 0;

    @Column(name = "create_date")
    private LocalDateTime createdDate;
   
    @Column(name = "short_url")
    private String shortUrl;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "urlMapping")
    private List<ClickEvent> clickEvents;


}

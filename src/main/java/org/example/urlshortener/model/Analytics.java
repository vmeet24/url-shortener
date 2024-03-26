package org.example.urlshortener.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String shortUrl;
    private LocalDateTime timestamp;
    private String referrer;
    private String userAgent;
    private String originalUrl;

    // Constructors, Getters, and Setters
    public Analytics() {}

    public Analytics(String shortUrl, String originalUrl, LocalDateTime timestamp, String referrer, String userAgent) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.timestamp = timestamp;
        this.referrer = referrer;
        this.userAgent = userAgent;
    }

    // Standard getters and setters
}

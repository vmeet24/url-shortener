package org.example.urlshortener.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.urlshortener.dto.RequestUrlDto;
import org.example.urlshortener.model.Analytics;
import org.example.urlshortener.model.Url;
import org.example.urlshortener.repository.AnalyticsRepository;
import org.example.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlService urlService;

    private final AnalyticsRepository analyticsRepository;

    public UrlController(UrlService urlService, AnalyticsRepository analyticsRepository) {
        this.urlService = urlService;
        this.analyticsRepository = analyticsRepository;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Url> shortenUrl(@RequestBody RequestUrlDto requestUrlDto) {
        Url url = urlService.shortenUrl(requestUrlDto.originalUrl().trim());
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletRequest request,
                                                      @RequestHeader(value = "referer", required = false) String referrer) {
        Url url = urlService.getOriginalUrl(shortLink);
        if (url != null) {
            String userAgent = request.getHeader("User-Agent");
            Analytics analytics = new Analytics(url.getShortLink(), url.getOriginalUrl(), LocalDateTime.now(), referrer, userAgent);
            analyticsRepository.save(analytics);

            String originalUrl = url.getOriginalUrl();
            if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
                originalUrl = "https://" + originalUrl;
            }
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
        }
        return ResponseEntity.notFound().build();
    }

}
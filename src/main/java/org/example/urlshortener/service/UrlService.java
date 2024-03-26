package org.example.urlshortener.service;


import org.example.urlshortener.model.Url;
import org.example.urlshortener.repository.UrlRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length(); // 62

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @CachePut(value = "urls", key = "#result.shortLink")
    public Url shortenUrl(String originalUrl) {
        Url url = new Url(originalUrl, "test");
        url = urlRepository.save(url); // Save to get the auto-generated ID
        String encodedId = encode(url.getId());
        url.setShortLink(encodedId);
        urlRepository.save(url); // Update with the short link
        return url;
    }

    @Cacheable(value = "urls", key = "#shortLink")
    public Url getOriginalUrl(String shortLink) {
        return urlRepository.findById(decode(shortLink)).orElse(null);
    }

    private String encode(long id) {
        StringBuilder encoded = new StringBuilder();
        while (id > 0) {
            encoded.insert(0, ALPHABET.charAt((int) (id % BASE)));
            id /= BASE;
        }
        return encoded.toString();
    }


    private long decode(String shortLink) {
        long id = 0;
        for (int i = 0; i < shortLink.length(); i++) {
            id = id * BASE + ALPHABET.indexOf(shortLink.charAt(i));
        }
        return id;
    }

}


package com.example.urlshortener.service;

import com.example.urlshortener.dto.ShortenResponse;
import com.example.urlshortener.entity.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import com.example.urlshortener.utils.Base62Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final ShortUrlRepository repository;
    private final Base62Encoder base62Encoder;

    @Transactional
    public ShortenResponse shortenUrl(String originalUrl) {
        ShortUrl url = new ShortUrl();
        url.setOriginalUrl(originalUrl);
        url.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // Expires in 5 mins
        
        ShortUrl savedUrl = repository.save(url);
        String shortCode = base62Encoder.encode(savedUrl.getId());
        savedUrl.setShortCode(shortCode);
        repository.save(savedUrl);

        return new ShortenResponse(shortCode, originalUrl, savedUrl.getCreatedAt());
    }

    // MAIN METHOD: This runs every time (Checking logic)
    public ShortUrl getOriginalUrl(String shortCode) {
        // 1. Fetch from Cache/DB
        ShortUrl url = fetchFromCache(shortCode);

        // 2. Check Expiry (This now runs even if data came from Cache)
        if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())) {
             throw new RuntimeException("Link has expired");
        }
        
        return url;
    }

    // HELPER METHOD: This handles the Caching
    @Cacheable(value = "short-urls", key = "#shortCode")
    public ShortUrl fetchFromCache(String shortCode) {
        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }

    @Transactional
    public void incrementClick(String shortCode) {
        ShortUrl url = repository.findByShortCode(shortCode).orElse(null);
        if (url != null) {
            url.setClickCount(url.getClickCount() + 1);
            repository.save(url);
        }
    }

    public ShortUrl getStats(String shortCode) {
        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }
}
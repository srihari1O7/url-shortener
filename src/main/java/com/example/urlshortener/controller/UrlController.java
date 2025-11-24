package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenRequest;
import com.example.urlshortener.dto.ShortenResponse;
import com.example.urlshortener.entity.ShortUrl;
import com.example.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

   
    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenResponse> shorten(@RequestBody ShortenRequest request) {
        ShortenResponse response = urlService.shortenUrl(request.getOriginalUrl());
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/s/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        
        ShortUrl url = urlService.getOriginalUrl(code);

    
        urlService.incrementClick(code);
        
        
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }

    
    @GetMapping("/api/info/{code}")
    public ResponseEntity<ShortUrl> getInfo(@PathVariable String code) {
        return ResponseEntity.ok(urlService.getStats(code));
    }
}
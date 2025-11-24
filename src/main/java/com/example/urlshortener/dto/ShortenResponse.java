package com.example.urlshortener.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShortenResponse {
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime createdAt;
}
package com.origin.linkshortener.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortenerMapping {
    private String urlCode;
    private String originalUrl;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private int urlAccessedCount = 0;

    public void incrementAccessCount() {
        this.urlAccessedCount++;
    }
}

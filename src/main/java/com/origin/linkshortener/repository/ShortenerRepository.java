package com.origin.linkshortener.repository;

import com.origin.linkshortener.model.ShortenedUrl;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ShortenerRepository {
    private final ConcurrentHashMap<String, ShortenedUrl> urlStore = new ConcurrentHashMap<>();

    public ShortenedUrl save(ShortenedUrl shortenedUrl) {
        urlStore.put(shortenedUrl.getUrlCode(), shortenedUrl);
        return shortenedUrl;
    }

    public Optional<ShortenedUrl> findByUrlCode(String urlCode) {
        return Optional.ofNullable(urlStore.get(urlCode));
    }

    public boolean existsByUrlCode(String urlCode) {
        return urlStore.containsKey(urlCode);
    }
}

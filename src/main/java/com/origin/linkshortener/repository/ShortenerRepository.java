package com.origin.linkshortener.repository;

import com.origin.linkshortener.model.ShortenerMapping;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ShortenerRepository {
    private final ConcurrentHashMap<String, ShortenerMapping> urlStore = new ConcurrentHashMap<>();

    public ShortenerMapping save(ShortenerMapping shortenerMapping) {
        urlStore.put(shortenerMapping.getUrlCode(), shortenerMapping);
        return shortenerMapping;
    }

    public Optional<ShortenerMapping> findByUrlCode(String urlCode) {
        return Optional.ofNullable(urlStore.get(urlCode));
    }

    public boolean existsByShortCode(String urlCode) {
        return urlStore.containsKey(urlCode);
    }
}

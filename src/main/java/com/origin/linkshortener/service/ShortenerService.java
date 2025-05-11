package com.origin.linkshortener.service;

import com.origin.linkshortener.exception.InvalidUrlException;
import com.origin.linkshortener.exception.UrlNotFoundException;
import com.origin.linkshortener.model.ShortenedUrl;
import com.origin.linkshortener.repository.ShortenerRepository;
import com.origin.linkshortener.utils.GenerateUniqueUrlCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortenerService {

    private final ShortenerRepository shortenerRepository;

    @Value("${url.shortener.code-length:6}")
    private int codeLength;

    public ShortenedUrl shortenUrl(String originalUrl) {
        validateUrl(originalUrl);

        String urlCode = generateUniqueUrlCode();
        ShortenedUrl shortenedUrl = ShortenedUrl.builder()
                .urlCode(urlCode)
                .originalUrl(originalUrl)
                .build();

        log.info("Generated url code: {} for URL: {}", urlCode, originalUrl);
        return shortenerRepository.save(shortenedUrl);
    }

    public ShortenedUrl getUrlMapping(String urlCode) {
        return shortenerRepository.findByUrlCode(urlCode)
                .orElseThrow(() -> new UrlNotFoundException("URL Code not found: " + urlCode));
    }

    private void validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new InvalidUrlException("URL cannot be empty");
        }

        try {
            new URI(url).toURL();
        } catch (URISyntaxException | IllegalArgumentException | java.net.MalformedURLException e) {
            throw new InvalidUrlException("Invalid URL format: " + url);
        }
    }

    private String generateUniqueUrlCode() {
        String urlCode;
        do {
            urlCode = GenerateUniqueUrlCode.generateUrlCode(codeLength);
        } while (shortenerRepository.existsByUrlCode(urlCode));

        return urlCode;
    }

}
package com.origin.linkshortener.mapper;

import com.origin.linkshortener.model.ShortenedUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UrlResponseMapper {

    @Value("${url.shortener.domain:https://orig.in/}")
    private String baseUrl;

    public Map<String, Object> toResponse(ShortenedUrl shortenedUrl) {
        return Map.of(
                "originalUrl", shortenedUrl.getOriginalUrl(),
                "shortUrl", baseUrl + shortenedUrl.getUrlCode(),
                "urlCode", shortenedUrl.getUrlCode()
        );
    }
}

package com.origin.linkshortener.mapper;

import com.origin.linkshortener.model.ShortenedUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UrlResponseMapperTest {

    private UrlResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UrlResponseMapper();
        ReflectionTestUtils.setField(mapper, "baseUrl", "http://orig.in/");
    }

    @Test
    void givenUrlMapperCalledWithCorrectValues_shouldReturnResponseWithCorrectMapping() {
        LocalDateTime now = LocalDateTime.now();
        ShortenedUrl shortenedUrl = ShortenedUrl.builder()
                .urlCode("abc123")
                .originalUrl("https://example.com")
                .build();

        Map<String, Object> response = mapper.toResponse(shortenedUrl);

        assertEquals("https://example.com", response.get("originalUrl"));
        assertEquals("http://orig.in/abc123", response.get("shortUrl"));
        assertEquals("abc123", response.get("urlCode"));
    }
}

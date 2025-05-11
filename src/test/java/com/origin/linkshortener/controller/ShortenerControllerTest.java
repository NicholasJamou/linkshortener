package com.origin.linkshortener.controller;

import com.origin.linkshortener.mapper.UrlResponseMapper;
import com.origin.linkshortener.model.ShortenedUrl;
import com.origin.linkshortener.service.ShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShortenerControllerTest {

    @Mock
    private ShortenerService shortenerService;

    @Mock
    private UrlResponseMapper responseMapper;

    private ShortenerController shortenerController;

    @BeforeEach
    void setUp() {
        shortenerController = new ShortenerController(shortenerService, responseMapper);
    }

    @Test
    void givenValidUrl_whenShortenUrl_thenReturnPostShortenedUrl() {
        String originalUrl = "https://www.originenergy.com.au/electricity-gas/plans.html";
        String urlCode = "abc123";

        Map<String, String> request = Map.of("url", originalUrl);

        ShortenedUrl shortenedUrl = ShortenedUrl.builder()
                .urlCode(urlCode)
                .originalUrl(originalUrl)
                .build();

        Map<String, Object> expectedResponse = Map.of(
                "originalUrl", originalUrl,
                "shortUrl", "https://orig.in/" + urlCode,
                "urlCode", urlCode
        );

        when(shortenerService.shortenUrl(originalUrl)).thenReturn(shortenedUrl);
        when(responseMapper.toResponse(shortenedUrl)).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = shortenerController.shortenUrl(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse, response.getBody());
        assertEquals(originalUrl, response.getBody().get("originalUrl"));
        assertEquals("https://orig.in/" + urlCode, response.getBody().get("shortUrl"));
        assertEquals(urlCode, response.getBody().get("urlCode"));

        verify(shortenerService).shortenUrl(originalUrl);
        verify(responseMapper).toResponse(shortenedUrl);
    }

    @Test
    void givenValidUrl_whenGetShortUrl_thenReturnGetShortenedUrl() {
        String originalUrl = "https://www.originenergy.com.au/electricity-gas/plans.html";
        String urlCode = "abc123";

        ShortenedUrl shortenedUrl = ShortenedUrl.builder()
                .urlCode(urlCode)
                .originalUrl(originalUrl)
                .build();

        Map<String, Object> expectedResponse = Map.of(
                "originalUrl", originalUrl,
                "shortUrl", "https://orig.in/" + urlCode,
                "urlCode", urlCode
        );

        when(shortenerService.getUrlMapping(urlCode)).thenReturn(shortenedUrl);
        when(responseMapper.toResponse(shortenedUrl)).thenReturn(expectedResponse);

        ResponseEntity<Map<String, Object>> response = shortenerController.getUrlInfo(urlCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse, response.getBody());

        verify(shortenerService).getUrlMapping(urlCode);
        verify(responseMapper).toResponse(shortenedUrl);
    }
}

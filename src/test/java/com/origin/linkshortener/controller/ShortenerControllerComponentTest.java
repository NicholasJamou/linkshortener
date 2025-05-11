package com.origin.linkshortener.controller;

import com.origin.linkshortener.exception.InvalidUrlException;
import com.origin.linkshortener.exception.UrlNotFoundException;
import com.origin.linkshortener.mapper.UrlResponseMapper;
import com.origin.linkshortener.model.ShortenedUrl;
import com.origin.linkshortener.service.ShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShortenerController.class)
public class ShortenerControllerComponentTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShortenerService shortenerService;
    @MockitoBean
    private UrlResponseMapper responseMapper;

    @Test
    void givenValidShortenUrl_postUrl_returns201() throws Exception {
        ShortenedUrl shortenedUrl = ShortenedUrl.builder()
                .urlCode("abc123")
                .originalUrl("https://example.com")
                .build();

        Map<String, Object> response = Map.of(
                "originalUrl", "https://example.com",
                "shortUrl", "https://orig.in/abc123",
                "urlCode", "abc123"
        );

        when(shortenerService.shortenUrl("https://example.com")).thenReturn(shortenedUrl);
        when(responseMapper.toResponse(shortenedUrl)).thenReturn(response);

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\": \"https://example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.urlCode").value("abc123"))
                .andExpect(jsonPath("$.originalUrl").value("https://example.com"));
    }

    @Test
    void givenInvalidShortenUrl_returns400() throws Exception {
        when(shortenerService.shortenUrl("invalid-url"))
                .thenThrow(new InvalidUrlException("Invalid URL format"));

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\": \"invalid-url\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid URL format"));
    }

    @Test
    void givenExistingUrlCode_getUrlInfo_returns200() throws Exception {
        ShortenedUrl existing = ShortenedUrl.builder()
                .urlCode("abc123")
                .originalUrl("https://example.com")
                .build();

        Map<String, Object> expectedResponse = Map.of(
                "urlCode", "abc123",
                "originalUrl", "https://example.com",
                "shortUrl", "https://orig.in/abc123"
        );

        when(shortenerService.getUrlMapping("abc123")).thenReturn(existing);
        when(responseMapper.toResponse(existing)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/info/abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.urlCode").value("abc123"))
                .andExpect(jsonPath("$.originalUrl").value("https://example.com"))
                .andExpect(jsonPath("$.shortUrl").value("https://orig.in/abc123"));
    }

    @Test
    void givenInvalidGetUrl_returns404() throws Exception {
        when(shortenerService.getUrlMapping("whereisit"))
                .thenThrow(new UrlNotFoundException("Short URL not found: whereisit"));

        mockMvc.perform(get("/api/info/whereisit"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Short URL not found: whereisit"));
    }
}

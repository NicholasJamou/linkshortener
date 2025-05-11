package com.origin.linkshortener.service;

import com.origin.linkshortener.exception.InvalidUrlException;
import com.origin.linkshortener.exception.UrlNotFoundException;
import com.origin.linkshortener.model.ShortenedUrl;
import com.origin.linkshortener.repository.ShortenerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShortenerServiceTest {

    @Mock
    private ShortenerRepository shortenerRepository;

    private ShortenerService shortenerService;

    @BeforeEach
    void setUp() {
        shortenerService = new ShortenerService(shortenerRepository);
        ReflectionTestUtils.setField(shortenerService, "codeLength", 6);
    }

    @Test
    void givenValidUrl_urlCodeDoesNotAlreadyExist_shouldReturn6DigitUniqueUrlCodeAndSaveInMemory() {
        String originalUrl = "https://www.originenergy.com.au/electricity-gas/plans.html";

        when(shortenerRepository.existsByUrlCode(anyString())).thenReturn(false);
        when(shortenerRepository.save(any(ShortenedUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShortenedUrl result = shortenerService.shortenUrl(originalUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertNotNull(result.getUrlCode());
        assertEquals(6, result.getUrlCode().length());
        verify(shortenerRepository).save(any(ShortenedUrl.class));
    }

    @Test
    void givenNullUrl_shouldThrowInvalidUrlExceptionExceptionAndNotSaveInMemory() {
        assertThrows(InvalidUrlException.class, () -> shortenerService.shortenUrl(null));
        verify(shortenerRepository, never()).save(any(ShortenedUrl.class));
    }

    @Test
    void givenEmptyUrl_shouldThrowInvalidUrlExceptionExceptionAndNotSaveInMemory() {
        assertThrows(InvalidUrlException.class, () -> shortenerService.shortenUrl(""));
        verify(shortenerRepository, never()).save(any(ShortenedUrl.class));
    }

    @Test
    void givenInvalidUrl_shouldThrowInvalidUrlExceptionExceptionAndNotSaveInMemory() {
        assertThrows(InvalidUrlException.class, () -> shortenerService.shortenUrl("origindotcom"));
        verify(shortenerRepository, never()).save(any(ShortenedUrl.class));
    }

    @Test
    void givenValidUrl_andGetUrlMappingIsCalled_shouldReturnShortenedUrl() {
        String urlCode = "abc123";
        String originalUrl = "https://www.originenergy.com.au/electricity-gas/plans.html";
        ShortenedUrl shortenedUrl = ShortenedUrl.builder()
                .urlCode(urlCode)
                .originalUrl(originalUrl)
                .build();

        when(shortenerRepository.findByUrlCode(urlCode)).thenReturn(Optional.of(shortenedUrl));

        ShortenedUrl result = shortenerService.getUrlMapping(urlCode);

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertEquals(urlCode, result.getUrlCode());
    }

    @Test
    void givenUrlCodeThatDoesntExist_shouldThrowUrlNotFoundException() {
        String urlCode = "origin123";
        when(shortenerRepository.findByUrlCode(urlCode)).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> shortenerService.getUrlMapping(urlCode));
    }

    @Test
    void givenGeneratedUrlCodeAlreadyExists_ensureTheApplicationRetries_shouldReturnUniqueCode() {
        //if the application generates a code that already exists, ensure it retries and finds a unique code
        when(shortenerRepository.existsByUrlCode(anyString())).thenReturn(true, false); //trying 2 times, first time is duplicate second time is not
        when(shortenerRepository.save(any(ShortenedUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShortenedUrl result = shortenerService.shortenUrl("https://example.com");

        assertNotNull(result.getUrlCode());
        verify(shortenerRepository, times(2)).existsByUrlCode(any());
    }

}

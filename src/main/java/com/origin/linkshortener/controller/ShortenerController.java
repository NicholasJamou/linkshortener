package com.origin.linkshortener.controller;

import com.origin.linkshortener.mapper.UrlResponseMapper;
import com.origin.linkshortener.model.ShortenedUrl;
import com.origin.linkshortener.service.ShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShortenerController {

    private final ShortenerService shortenerService;
    private final UrlResponseMapper urlResponseMapper;

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, Object>> shortenUrl(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        ShortenedUrl shortenedUrl = shortenerService.shortenUrl(url);
        return new ResponseEntity<>(urlResponseMapper.toResponse(shortenedUrl), HttpStatus.CREATED);
    }

    @GetMapping("/info/{urlCode}")
    public ResponseEntity<Map<String, Object>> getUrlInfo(@PathVariable String urlCode) {
        ShortenedUrl shortenedUrl = shortenerService.getUrlMapping(urlCode);
        return ResponseEntity.ok(urlResponseMapper.toResponse(shortenedUrl));
    }

}

package com.origin.linkshortener.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerateUniqueUrlCode {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateUrlCode(int length) {
        byte[] randomBytes = new byte[length];
        RANDOM.nextBytes(randomBytes);

        String base64 = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        return base64.substring(0, Math.min(length, base64.length()));
    }
}

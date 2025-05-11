package com.origin.linkshortener.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class UniqueUrlCodeTest {

    @Test
    void givenGenerateUrlCodeUtilCalled_shouldReturnUrlCodeWith6Chars() {
        String uniqueCode = GenerateUniqueUrlCode.generateUrlCode(6);
        Assertions.assertEquals(6, uniqueCode.length());
    }

    @Test
    void givenGenerateUrlCodeUtilCalled_shouldReturnUrlCodeWithUrlFriendlyCharacters() {
        String code = GenerateUniqueUrlCode.generateUrlCode(100);

        assertFalse(code.contains("+"));
        assertFalse(code.contains("/"));
        assertFalse(code.contains("="));
    }
}

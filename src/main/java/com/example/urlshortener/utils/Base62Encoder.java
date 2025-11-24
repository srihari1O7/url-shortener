package com.example.urlshortener.utils;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {

    private static final String ALLOWED_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALLOWED_CHARACTERS.length();

    public String encode(long input) {
        StringBuilder encodedString = new StringBuilder();

        if (input == 0) {
            return String.valueOf(ALLOWED_CHARACTERS.charAt(0));
        }

        while (input > 0) {
            encodedString.append(ALLOWED_CHARACTERS.charAt((int) (input % BASE)));
            input = input / BASE;
        }

        return encodedString.reverse().toString();
    }

    public long decode(String input) {
        char[] characters = input.toCharArray();
        int length = characters.length;
        long decoded = 0;
        long counter = 1;

        for (int i = 0; i < length; i++) {
            decoded += ALLOWED_CHARACTERS.indexOf(characters[length - 1 - i]) * counter;
            counter *= BASE;
        }
        return decoded;
    }
}
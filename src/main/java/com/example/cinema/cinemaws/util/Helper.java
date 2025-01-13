package com.example.cinema.cinemaws.util;

import java.security.SecureRandom;
import java.util.Map;
import java.util.regex.Pattern;

public class Helper {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Validates the given email address.
     *
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Generates a random OTP (One-Time Password) of the specified length.
     *
     * @param length the length of the OTP
     * @return a random OTP as a string
     */
    public static String generateRandomOTP(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(RANDOM.nextInt(10));
        }
        return otp.toString();
    }

    /**
     * Replaces placeholders in the template with corresponding values from the map.
     *
     * @param template the template string with placeholders
     * @param values   a map containing key-value pairs for replacement
     * @return the formatted string with placeholders replaced by actual values
     */
    public static String replacePlaceholders(String template, Map<String, String> values) {
        String formattedString = template;

        for (Map.Entry<String, String> entry : values.entrySet()) {
            String placeholder = String.format("{{%s}}", entry.getKey());
            formattedString = formattedString.replace(placeholder, entry.getValue() != null ? entry.getValue() : "");
        }

        return formattedString;
    }
}
package com.example.cinema.cinemaws.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HelperTest {

    @Test
    public void givenValidEmailWhenIsValidEmailThenReturnTrue() {
        assertTrue(Helper.isValidEmail("test@example.com"));
        assertTrue(Helper.isValidEmail("user.mail@example.com"));
        assertTrue(Helper.isValidEmail("user@company.example.com"));
    }

    @Test
    public void givenInvalidEmailWhenIsValidEmailThenReturnFalse() {
        assertFalse(Helper.isValidEmail(null));
        assertFalse(Helper.isValidEmail(""));
        assertFalse(Helper.isValidEmail("NotAnEmail"));
        assertFalse(Helper.isValidEmail("@example.com"));
        assertFalse(Helper.isValidEmail("mail@.com"));
    }

    @Test
    public void givenLengthWhenGenerateRandomOtpThenReturnStringOfSpecifiedLength() {
        String otp = Helper.generateRandomOTP(6);
        assertEquals(6, otp.length());

        otp = Helper.generateRandomOTP(4);
        assertEquals(4, otp.length());
    }

    @Test
    public void givenInvalidLengthWhenGenerateRandomOtpThenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Helper.generateRandomOTP(0));
        assertThrows(IllegalArgumentException.class, () -> Helper.generateRandomOTP(-1));
    }

    @Test
    public void givenTemplateAndValuesWhenReplacePlaceholdersThenReturnReplacedString() {
        String template = "Hello, {{name}}! Your OTP is {{otp}}.";
        Map<String, String> values = new HashMap<>();
        values.put("name", "Robert");
        values.put("otp", "123456");

        String result = Helper.replacePlaceholders(template, values);
        assertEquals("Hello, Robert! Your OTP is 123456.", result);
    }

    @Test
    public void givenTemplateWithMissingValuesWhenReplacePlaceholdersThenHandleMissingValues() {
        String template = "Hello, {{name}}! Your OTP is {{otp}}.";
        Map<String, String> values = new HashMap<>();
        values.put("name", null);
        values.put("otp", "123456");

        String result = Helper.replacePlaceholders(template, values);
        assertEquals("Hello, ! Your OTP is 123456.", result);
    }

    @Test
    public void givenTemplateWhenReplacePlaceholdersWithNoValuesThenReturnOriginalTemplate() {
        String template = "Hello, {{name}}! Your OTP is {{otp}}.";
        String result = Helper.replacePlaceholders(template, new HashMap<>());
        assertEquals("Hello, {{name}}! Your OTP is {{otp}}.", result);
    }
}
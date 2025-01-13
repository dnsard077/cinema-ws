package com.example.cinema.cinemaws.util;

import com.example.cinema.cinemaws.dto.TransactionTypeEn;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTemplateTest {

    @Test
    public void givenRegisterTransactionTypeWhenGetOtpEmailTemplateThenReturnCorrectTemplate() {
        Map<String, String> values = new HashMap<>();
        values.put("name", "John Doe");
        values.put("otp", "123456");

        String result = EmailTemplate.getOtpEmailTemplate(TransactionTypeEn.REGISTER, values);

        String expected = "<html>" +
                "<body>" +
                "<h2>Hi John Doe,</h2>" +
                "<p>Welcome to <strong>Example Cinema</strong>! We're excited to have you on board.</p>" +
                "<p>To complete your registration, please use the One-Time Password (OTP) below:</p>" +
                "<h3 style='color: #007BFF;'>123456</h3>" +
                "<p>This code is valid for 2 minutes, so make sure to enter it soon!</p>" +
                "<p>If you didn’t request this OTP, no worries! Just ignore this email or reach out to our support team if you have any questions.</p>" +
                "<p>Cheers,<br>The Example Cinema Team</p>" +
                "</body>" +
                "</html>";

        assertEquals(expected, result);
    }

    @Test
    public void givenWelcomeTransactionTypeWhenGetOtpEmailTemplateThenReturnCorrectTemplate() {
        Map<String, String> values = new HashMap<>();
        values.put("name", "Jane Doe");

        String result = EmailTemplate.getOtpEmailTemplate(TransactionTypeEn.WELCOME, values);

        String expected = "<html>" +
                "<body>" +
                "<h2>Hi Jane Doe,</h2>" +
                "<p>Congratulations on successfully registering with <strong>Example Cinema</strong>!</p>" +
                "<p>We’re thrilled to have you as part of our community. You can now explore our wide range of movies and enjoy exclusive offers just for you.</p>" +
                "<p>If you have any questions or need assistance, feel free to reach out to our support team. We're here to help!</p>" +
                "<p>Happy watching!</p>" +
                "<p>Best wishes,<br>The Example Cinema Team</p>" +
                "</body>" +
                "</html>";

        assertEquals(expected, result);
    }

    @Test
    public void givenInvalidTransactionTypeWhenGetOtpEmailTemplateThenThrowException() {
        Map<String, String> values = new HashMap<>();
        values.put("name", "John Doe");
        values.put("otp", "123456");

        TransactionTypeEn invalidType = null;

        assertThrows(IllegalArgumentException.class, () -> {
            EmailTemplate.getOtpEmailTemplate(invalidType, values);
        });
    }

    @Test
    public void givenTransactionTypeWithMissingValuesWhenGetOtpEmailTemplateThenHandleMissingValues() {
        Map<String, String> values = new HashMap<>();
        values.put("name", null);
        values.put("otp", "654321");

        String result = EmailTemplate.getOtpEmailTemplate(TransactionTypeEn.REGISTER, values);

        String expected = "<html>" +
                "<body>" +
                "<h2>Hi ,</h2>" +
                "<p>Welcome to <strong>Example Cinema</strong>! We're excited to have you on board.</p>" +
                "<p>To complete your registration, please use the One-Time Password (OTP) below:</p>" +
                "<h3 style='color: #007BFF;'>654321</h3>" +
                "<p>This code is valid for 2 minutes, so make sure to enter it soon!</p>" +
                "<p>If you didn’t request this OTP, no worries! Just ignore this email or reach out to our support team if you have any questions.</p>" +
                "<p>Cheers,<br>The Example Cinema Team</p>" +
                "</body>" +
                "</html>";

        assertEquals(expected, result);
    }
}
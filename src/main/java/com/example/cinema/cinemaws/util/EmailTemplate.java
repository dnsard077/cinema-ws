package com.example.cinema.cinemaws.util;

import com.example.cinema.cinemaws.dto.TransactionTypeEn;

import java.util.HashMap;
import java.util.Map;

public class EmailTemplate {
    private static final Map<TransactionTypeEn, String> emailTemplates = new HashMap<>();

    static {
        emailTemplates.put(TransactionTypeEn.REGISTER,
                "<html>" +
                        "<body>" +
                        "<h2>Hi {{name}},</h2>" +
                        "<p>Welcome to <strong>Example Cinema</strong>! We're excited to have you on board.</p>" +
                        "<p>To complete your registration, please use the One-Time Password (OTP) below:</p>" +
                        "<h3 style='color: #007BFF;'>{{otp}}</h3>" +
                        "<p>This code is valid for 2 minutes, so make sure to enter it soon!</p>" +
                        "<p>If you didn’t request this OTP, no worries! Just ignore this email or reach out to our support team if you have any questions.</p>" +
                        "<p>Cheers,<br>The Example Cinema Team</p>" +
                        "</body>" +
                        "</html>");

        emailTemplates.put(TransactionTypeEn.WELCOME,
                "<html>" +
                        "<body>" +
                        "<h2>Hi {{name}},</h2>" +
                        "<p>Congratulations on successfully registering with <strong>Example Cinema</strong>!</p>" +
                        "<p>We’re thrilled to have you as part of our community. You can now explore our wide range of movies and enjoy exclusive offers just for you.</p>" +
                        "<p>If you have any questions or need assistance, feel free to reach out to our support team. We're here to help!</p>" +
                        "<p>Happy watching!</p>" +
                        "<p>Best wishes,<br>The Example Cinema Team</p>" +
                        "</body>" +
                        "</html>");
    }

    public static String getOtpEmailTemplate(TransactionTypeEn trxType, Map<String, String> values) {
        String template = emailTemplates.get(trxType);
        if (template == null) {
            throw new IllegalArgumentException("No template found for transaction type: " + trxType);
        }
        return Helper.replacePlaceholders(template, values);
    }
}
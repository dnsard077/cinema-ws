package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.EmailReqTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final ValidationService validationService;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${development.email}")
    private String developmentEmail;

    public void sendEmail(EmailReqTO request) {
        validationService.validate(request);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(request.from());
            helper.setTo(activeProfile != null && !activeProfile.equalsIgnoreCase("prod") ? developmentEmail : request.to());
            String[] ccArray = request.cc() != null ? request.cc().toArray(new String[0]) : new String[0];
            String[] bccArray = request.bcc() != null ? request.bcc().toArray(new String[0]) : new String[0];
            helper.setCc(ccArray);
            helper.setBcc(bccArray);
            helper.setSubject(request.subject());
            helper.setText(request.content(), true);
            if (request.replyTo() != null && !request.replyTo().isEmpty()) {
                helper.setReplyTo(request.replyTo());
            }

            mailSender.send(message);
            log.info("Email sent to: {}", request.to());
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", request.to(), e.getMessage());
            throw new ResponseException(ResponseCodeEn.OTP_FAILED);
        }
    }
}
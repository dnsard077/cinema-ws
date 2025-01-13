package com.example.cinema.cinemaws.service;

import com.example.cinema.cinemaws.dto.EmailReqTO;
import com.example.cinema.cinemaws.dto.ResponseCodeEn;
import com.example.cinema.cinemaws.exception.ResponseException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestPropertySource(properties = "spring.profiles.active=test")
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ValidationService validationService;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(mimeMessageHelper.getMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void givenValidEmailRequest_whenSendEmail_thenEmailIsSent() throws Exception {
        // Given
        EmailReqTO request = EmailReqTO.builder()
                .from("sender@example.com")
                .to("recipient@example.com")
                .subject("Test Subject")
                .content("Test Content")
                .build();
        doNothing().when(validationService).validate(request);

        // When
        emailService.sendEmail(request);

        // Then
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void givenInvalidEmailRequest_whenSendEmail_thenThrowResponseException() {
        // Given
        EmailReqTO request = EmailReqTO.builder()
                .from("sender@example.com")
                .to("recipient@example.com")
                .subject("Test Subject")
                .content("Test Content")
                .build();

        // Mock validation to throw an exception
        doThrow(new ResponseException(ResponseCodeEn.OTP_FAILED)).when(validationService).validate(request);

        // When & Then
        assertThrows(ResponseException.class, () -> emailService.sendEmail(request));
    }

    @Test
    void givenEmailService_whenSendEmailFails_thenThrowResponseException() throws Exception {
        // Given
        EmailReqTO request = EmailReqTO.builder()
                .from("sender@example.com")
                .to("recipient@example.com")
                .subject("Test Subject")
                .content("Test Content")
                .build();

        doNothing().when(validationService).validate(request);
        doThrow(new RuntimeException("Email sending failed")).when(mailSender).send(any(MimeMessage.class));

        // When & Then
        assertThrows(ResponseException.class, () -> emailService.sendEmail(request));
    }
}
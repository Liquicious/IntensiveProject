package org.example.notificationservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    @CircuitBreaker(name = "emailService", fallbackMethod = "fallback")
    public void sendEmail(String to, String subject, String text) {
        log.info("Sending email to: {}, Subject: {}", to, subject);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
        log.info("Email sent successfully to: {}", to);
    }

    @Override
    @CircuitBreaker(name = "emailService", fallbackMethod = "fallback")
    public void sendUserCreatedEmail(String to, String userName) {
        String subject = "Аккаунт успешно создан";
        String text = "Здравствуйте! Ваш аккаунт на сайте localhost:8080/api/users был успешно создан.";

        sendEmail(to, subject, text);
    }

    @Override
    @CircuitBreaker(name = "emailService", fallbackMethod = "fallback")
    public void sendUserDeletedEmail(String to, String userName) {
        String subject = "Аккаунт удален";
        String text = "Здравствуйте! Ваш аккаунт был удалён.";

        sendEmail(to, subject, text);
    }

    private void fallback(String to, String subject, String text, Throwable t) {
        log.warn("General email notification sending error. Message: {}", t.getMessage());
        System.out.println("General email notification sending error. Message: " + t.getMessage());
    }

    private void fallback(String to, String userName, Throwable t) {
        log.warn("Failed to send creation or deletion email notification. Message: {}", t.getMessage());
        System.out.println("Failed to send creation or deletion email notification. Message: " + t.getMessage());
    }
}
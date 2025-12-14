package org.example.notificationservice.service;

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
    public void sendEmail(String to, String subject, String text) {
        log.info("Sending email to: {}, Subject: {}", to, subject);

        if (isFakeEmailEnabled()) {
            logFakeEmail(to, subject, text);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
        log.info("Email sent successfully to: {}", to);
    }

    @Override
    public void sendUserCreatedEmail(String to, String userName) {
        String subject = "Аккаунт успешно создан";
        String text = "Здравствуйте! Ваш аккаунт на сайте localhost:8080/api/users был успешно создан.";

        sendEmail(to, subject, text);
    }

    @Override
    public void sendUserDeletedEmail(String to, String userName) {
        String subject = "Аккаунт удален";
        String text = "Здравствуйте! Ваш аккаунт был удалён.";

        sendEmail(to, subject, text);
    }

    private boolean isFakeEmailEnabled() {
        return false;
    }

    private void logFakeEmail(String to, String subject, String text) {
        log.info("""
                ===== FAKE EMAIL =====
                To: {}
                Subject: {}
                Text: {}
                ======================
                """, to, subject, text);
    }
}
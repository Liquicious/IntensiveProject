package org.example.notificationservice.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
    void sendUserCreatedEmail(String to, String userName);
    void sendUserDeletedEmail(String to, String userName);
}
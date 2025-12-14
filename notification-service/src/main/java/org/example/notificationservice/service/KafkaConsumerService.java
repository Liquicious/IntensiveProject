package org.example.notificationservice.service;

import org.example.notificationservice.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final EmailService emailService;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consumeUserEvent(UserEvent event) {
        log.info("Received user event: {} for email: {}", event.getEventType(), event.getEmail());

        try {
            switch (event.getEventType()) {
                case "USER_CREATED":
                    emailService.sendUserCreatedEmail(event.getEmail(), event.getUserName());
                    break;
                case "USER_DELETED":
                    emailService.sendUserDeletedEmail(event.getEmail(), event.getUserName());
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing user event for email: {}", event.getEmail(), e);
        }
    }
}
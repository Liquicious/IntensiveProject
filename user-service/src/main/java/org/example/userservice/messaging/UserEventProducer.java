package org.example.userservice.messaging;

import org.example.userservice.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public void sendUserEvent(UserEvent event) {
        log.info("Sending user event: {} for email: {}", event.getEventType(), event.getEmail());
        kafkaTemplate.send(TOPIC, event.getEmail(), event);
    }
}
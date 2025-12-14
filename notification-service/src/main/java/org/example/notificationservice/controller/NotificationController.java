package org.example.notificationservice.controller;

import org.example.notificationservice.dto.EmailRequest;
import org.example.notificationservice.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailRequest request) {
        log.info("Sending email to: {}", request.getTo());

        emailService.sendEmail(
                request.getTo(),
                request.getSubject(),
                request.getContent()
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/email/user-created")
    public ResponseEntity<Void> sendUserCreatedEmail(
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "Пользователь") String userName) {
        log.info("Sending user created email to: {}", email);

        emailService.sendUserCreatedEmail(email, userName);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/email/user-deleted")
    public ResponseEntity<Void> sendUserDeletedEmail(
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "Пользователь") String userName) {
        log.info("Sending user deleted email to: {}", email);

        emailService.sendUserDeletedEmail(email, userName);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
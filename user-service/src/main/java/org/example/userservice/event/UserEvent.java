package org.example.userservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String eventType;
    private String email;
    private String userName;
    private Long userId;
    private LocalDateTime timestamp;
}
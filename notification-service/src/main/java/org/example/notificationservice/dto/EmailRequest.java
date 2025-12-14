package org.example.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String to;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Content is required")
    private String content;
}
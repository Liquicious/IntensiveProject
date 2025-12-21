package org.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Имя пользователя", example = "Updated Name", minLength = 2, maxLength = 100)
    private String name;

    @Email(message = "Email should be valid")
    @Schema(description = "Email пользователя", example = "updated@example.com")
    private String email;

    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 120, message = "Age cannot be more than 120")
    @Schema(description = "Возраст пользователя", example = "30", minimum = "0", maximum = "120")
    private Integer age;
}
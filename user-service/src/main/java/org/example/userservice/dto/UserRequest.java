package org.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание пользователя")
public class UserRequest {
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Имя пользователя", example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 100)
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Schema(description = "Email пользователя", example = "john@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 120, message = "Age cannot be more than 120")
    @Schema(description = "Возраст пользователя", example = "25", minimum = "0", maximum = "120")
    private int age;
}
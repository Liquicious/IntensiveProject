package org.example.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ответ с информацией о пользователе")
public class UserResponse {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "John Johnson")
    private String name;

    @Schema(description = "Email пользователя", example = "john@example.com")
    private String email;

    @Schema(description = "Возраст пользователя", example = "25")
    private int age;

    @Schema(description = "Дата создания", example = "2024-12-15T10:30:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
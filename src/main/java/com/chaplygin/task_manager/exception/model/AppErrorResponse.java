package com.chaplygin.task_manager.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "App error response")
public class AppErrorResponse {
    @Schema(description = "HTTP Status", example = "401")
    int status;
    @Schema(description = "Message", example = "Access token expired")
    String message;
    @Schema(description = "Timestamp", example = "2024-06-04T22:40:46.7924577")
    LocalDateTime timestamp;

    public AppErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}

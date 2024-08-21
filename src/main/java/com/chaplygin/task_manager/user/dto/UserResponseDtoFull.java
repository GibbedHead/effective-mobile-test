package com.chaplygin.task_manager.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User response DTO with full info")
public record UserResponseDtoFull(
        @Schema(description = "User Id", example = "1")
        Long id,
        @Schema(description = "User email", example = "user@domain.com")
        String email,
        @Schema(description = "Username", example = "User1")
        String username
) {
}

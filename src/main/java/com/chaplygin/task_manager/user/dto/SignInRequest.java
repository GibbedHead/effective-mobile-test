package com.chaplygin.task_manager.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Schema(description = "SignIn request")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SignInRequest {
    @Schema(description = "User email", example = "user@domain.com")
    @Size(min = 6, max = 50)
    @NotBlank
    String email;

    @Schema(description = "Password", example = "mY_password")
    @NotBlank
    @Size(min = 1, max = 20)
    String password;
}

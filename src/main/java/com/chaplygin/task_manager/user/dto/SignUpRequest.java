package com.chaplygin.task_manager.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "SignUp request")
@AllArgsConstructor
public class SignUpRequest {

    @Schema(description = "User email", example = "user_1@domain.com")
    @NotBlank
    @Size(min = 6, max = 50)
    @Email
    String email;

    @Schema(description = "Password", example = "mY_password")
    @NotBlank
    @Size(min = 1, max = 20)
    String password;

    @Schema(description = "Username", example = "user_1")
    @Size(min = 3, max = 50)
    @NotBlank
    String username;

}

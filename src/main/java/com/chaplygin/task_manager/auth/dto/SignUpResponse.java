package com.chaplygin.task_manager.auth.dto;

import com.chaplygin.task_manager.user.dto.UserResponseDtoFull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "SignUp response with new User and Access token")
public class SignUpResponse {
    @Schema(implementation = UserResponseDtoFull.class)
    UserResponseDtoFull user;
    @Schema(description = "Access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.Sf...")
    String accessToken;
}

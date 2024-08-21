package com.chaplygin.task_manager.auth.controller;

import com.chaplygin.task_manager.auth.dto.SignInResponse;
import com.chaplygin.task_manager.auth.dto.SignUpResponse;
import com.chaplygin.task_manager.auth.service.AuthService;
import com.chaplygin.task_manager.exception.model.AppErrorResponse;
import com.chaplygin.task_manager.user.dto.SignInRequest;
import com.chaplygin.task_manager.user.dto.SignUpRequest;
import com.chaplygin.task_manager.user.mapper.UserMapper;
import com.chaplygin.task_manager.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication controller", description = "Endpoints for creating/logging users")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @Operation(summary = "SignUp", description = "Signup new user",
            responses = {
                    @ApiResponse(description = "Object, containing access token",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SignInResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                 "accessToken": "eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MTc1Mjc1NTUsImV4cCI6MTcxNzUyODE1NSwic3ViIjoiTWFsdmluYSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdfQ.utQS9pSI-CBEx-s8P2O1YsVwe7ofjQLX-YAj8b3yZ9Y-817TXbzlnuUwdOqahKHX"
                                             }""")
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            })
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = userMapper.mapSignUpRequestToUser(signUpRequest);
        return authService.signUp(user);
    }

    @Operation(summary = "SignIn", description = "Signin existed user",
            responses = {
                    @ApiResponse(description = "Object, containing access token",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SignInResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                 "accessToken": "eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MTc1Mjc1NTUsImV4cCI6MTcxNzUyODE1NSwic3ViIjoiTWFsdmluYSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdfQ.utQS9pSI-CBEx-s8P2O1YsVwe7ofjQLX-YAj8b3yZ9Y-817TXbzlnuUwdOqahKHX"
                                             }""")
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 401,
                                                "message": "Authentication error: 'Bad credentials'",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            })
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public SignInResponse signIn(@Valid @RequestBody SignInRequest signInRequest) {
        User user = userMapper.mapSignInRequestToUser(signInRequest);
        return authService.signIn(user);
    }

}

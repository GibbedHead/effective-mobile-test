package com.chaplygin.task_manager.auth.service;

import com.chaplygin.task_manager.auth.dto.SignInResponse;
import com.chaplygin.task_manager.auth.dto.SignUpResponse;
import com.chaplygin.task_manager.exception.model.InvalidUserException;
import com.chaplygin.task_manager.testDataFactory.UserFactory;
import com.chaplygin.task_manager.user.dto.UserResponseDtoFull;
import com.chaplygin.task_manager.user.mapper.UserMapper;
import com.chaplygin.task_manager.user.model.Role;
import com.chaplygin.task_manager.user.model.User;
import com.chaplygin.task_manager.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    public void givenValidUser_whenSignUp_thenReturnAccessTokenResponse() {
        User userFromRequest = UserFactory.createUser1FromRequest();
        User savedUser = UserFactory.createUser1Saved();
        UserResponseDtoFull dtoFull = new UserResponseDtoFull(1L, "email", "username");

        given(passwordEncoder.encode(userFromRequest.getPassword()))
                .willReturn("encodedPassword");
        given(userService.createUser(userFromRequest))
                .willReturn(savedUser);
        given(jwtService.generateAccessToken(anyString(), any(Role.class)))
                .willReturn("accessToken");
        given(userMapper.mapUserToUserResponseDtoFull(any()))
                .willReturn(dtoFull);

        SignUpResponse response = authService.signUp(userFromRequest);

        assertThat("accessToken").isEqualTo(response.getAccessToken());
    }

    @Test
    public void givenInvalidUser_whenSignUp_thenThrowInvalidUserException() {
        User userFromRequest = UserFactory.createUser1FromRequest();

        given(userService.createUser(userFromRequest))
                .willThrow(new InvalidUserException("Username already exists"));

        assertThatThrownBy(() -> authService.signUp(userFromRequest))
                .isInstanceOf(InvalidUserException.class);
    }

    @Test
    public void givenValidUser_whenSignIn_thenReturnAccessTokenResponse() {
        User userFromRequest = UserFactory.createUser1FromRequest();
        User foundUser = UserFactory.createUser1Saved();

        given(userService.findByEmail(anyString()))
                .willReturn(Optional.of(foundUser));
        given(jwtService.generateAccessToken(anyString(), any(Role.class)))
                .willReturn("accessToken");

        SignInResponse response = authService.signIn(userFromRequest);

        assertThat("accessToken").isEqualTo(response.getAccessToken());
    }

    @Test
    public void givenInvalidUser_whenSignIn_thenThrowBadCredentialsException() {
        User userFromRequest = UserFactory.createUser1FromRequest();

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.signIn(userFromRequest))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    public void givenNonExistedUser_whenSignIn_thenThrowUsernameNotFoundException() {
        User userFromRequest = UserFactory.createUser1FromRequest();

        given(userService.findByEmail(anyString()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.signIn(userFromRequest))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}